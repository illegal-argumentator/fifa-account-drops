package com.daniel.fifa_account_drops.adapter.account.out.drops;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserCdpInitializer;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.IdentityProtectionBrowserPort;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.ProfileResponse;
import com.daniel.fifa_account_drops.adapter.captcha.CaptchaClient;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.AccountCommandPort;
import com.daniel.fifa_account_drops.port.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.shared.ExecutorUtils;
import com.daniel.fifa_account_drops.shared.WaitUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

import static com.daniel.fifa_account_drops.adapter.account.out.drops.FifaDropsConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
final class AccountDropsCommandAdapter implements AccountDropsCommandPort {

    private final ExecutorService executor;
    private final CaptchaClient captchaClient;
    private final BrowserCdpInitializer cdpInitializer;
    private final AccountCommandPort accountCommandPort;
    private final IdentityProtectionBrowserPort identityProtectionBrowserPort;

    @Value("${threads.count}")
    private int threads;
    private final Semaphore semaphore = new Semaphore(threads);

    @Value("${browser.timeout.default}")
    private long DEFAULT_TIMEOUT;
    @Value("${browser.timeout.queue}")
    private long QUEUE_TIMEOUT;
    @Value("${accounts.persistence.enabled}")
    private boolean isPersistenceEnabled;
    @Value("${app.proxies.enabled}")
    private boolean PROXIES_ENABLED;
    @Value("${app.proxies}")
    private List<String> proxies;

    @Override
    public void process(List<Account> accounts) {
        try {
            List<CompletableFuture<Void>> futures = accounts.stream()
                    .map(account -> CompletableFuture.runAsync(() -> {
                        try {
                            semaphore.acquire();

                            log.info("Processing {}", account.email());
                            Optional<Account> result = process(account);
                            finishProcess(result);

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            semaphore.release();
                        }
                    }, executor))
                    .toList();

            futures.forEach(CompletableFuture::join);
        } finally {
            ExecutorUtils.shutDownGracefully(executor);
        }
    }

    private Optional<Account> process(Account account) {
        ProfileResponse profile = identityProtectionBrowserPort.createProfile(account.firstName(), getProxy());
        try (Browser browser = cdpInitializer.init(profile.getData().getProfileId())) {

            navigateToFifa(browser);
            goToLogIn(browser);
            waitInQueue(browser);
            processLogIn(account, browser);
            allowDrops(account, browser);

        } catch (Exception e) {
            log.error("Exception while processing account: {}.", e.getMessage());
            return Optional.empty();
        }

        return Optional.of(account);
    }

    private void navigateToFifa(Browser browser) throws Exception {
        log.info("Navigating.");
        browser.navigate(BASE_URL, ACCESS_DENIED_H1, () -> new RuntimeException("Access Denied."));
    }

    private void goToLogIn(Browser browser) {
        browser.click(LOG_IN_BUTTON);
        browser.waitForSelector(QUEUE_BUTTON);
    }

    private void waitInQueue(Browser browser) throws Exception {
        log.info("Waiting in queue.");
        browser.click(QUEUE_BUTTON);

        if (browser.isVisible(NUMBERS_CAPTCHA_IMG, DEFAULT_TIMEOUT / 6)) {
            String code = handleNumbersCaptcha(browser);
            log.info("Number captcha result: {}.", code);
        }
        browser.waitForSelector(EMAIL_INPUT, RESTRICTED_H1, QUEUE_TIMEOUT, () -> new RuntimeException("Processing is blocked"));
    }

    private String handleNumbersCaptcha(Browser browser) {
        log.info("Numbers captcha detected.");
        byte[] imageBytes = browser.screenshot(NUMBERS_CAPTCHA_IMG);
        return captchaClient.handleNumbers(imageBytes);
    }

    private void processLogIn(Account account, Browser browser) {
        log.info("Logging in.");
        browser.fillHumanly(EMAIL_INPUT, account.email());
        browser.fillHumanly(PASSWORD_INPUT, account.password());

        WaitUtils.waitSafely(1);
        browser.click(LOGIN_BUTTON);

        if (browser.isVisible(DEFAULT_CAPTCHA_ID, DEFAULT_TIMEOUT / 6)) {
            log.info("Default captcha spotted.");
            waitForCaptchaSolution(browser);
        }

        if (browser.isVisible(INVALID_CREDS_DIV, DEFAULT_TIMEOUT / 6)) {
            throw new RuntimeException("Invalid credentials for %s.".formatted(account.email()));
        }

        browser.waitForSelector(DROPS_CHECKBOX);
    }

    private void waitForCaptchaSolution(Browser browser) {
        if (browser.isVisible(DEFAULT_CAPTCHA_SOLVER_DIV, DEFAULT_TIMEOUT / 6)) {
            log.info("Solving.");
            int attempts = 15;
            while (browser.isVisible(DEFAULT_CAPTCHA_SOLVING_TEXT) && attempts != 0) {
                WaitUtils.waitSafely(5);
                attempts--;
                log.info("Waiting for captcha solution: {}.", attempts);
            }
        }
    }

    private void allowDrops(Account account, Browser browser) {
        if (browser.isVisible(DROPS_CHECKED)) {
            log.info("Account {} already has drops.", account.email());
            return;
        }

        log.info("Allowing drops.");
        browser.click(DROPS_CHECKBOX);
        browser.click(SAVE_BUTTON);

        WaitUtils.waitSafely(3);

        browser.waitForSelector(SUCCESS_TITLE);
        log.info("Successfully enabled drops for: {}.", account.email());
    }

    private String getProxy() {
        String proxy = null;
        if (PROXIES_ENABLED) {
            proxy = proxies.get(ThreadLocalRandom.current().nextInt(0, proxies.size()));
            log.info("Using proxy: {}.", proxy);
        }

        boolean clearProxy = new Random().nextInt(100) <= 20;
        return clearProxy ? null : proxy;
    }

    private void finishProcess(Optional<Account> account) {
        if (!isPersistenceEnabled || account.isEmpty()) {
            log.warn("Skipping account finishing.");
            return;
        }

        Account response = account.get();
        accountCommandPort.update(response.id(), Status.REGISTERED);
        log.info("Successfully finished account: {}.", response.email());
    }
}
