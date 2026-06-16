package com.daniel.fifa_account_drops.adapter.account.out.drops;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserCdpInitializer;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.IdentityProtectionBrowserPort;
import com.daniel.fifa_account_drops.adapter.browser.out.nst.dto.ProfileResponse;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.AccountCommandPort;
import com.daniel.fifa_account_drops.port.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.shared.ExecutorUtils;
import kotlin.text.UStringsKt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.daniel.fifa_account_drops.adapter.account.out.drops.FifaDropsConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
final class AccountDropsCommandAdapter implements AccountDropsCommandPort {

    @Value("${browser.timeout.queue}")
    private long QUEUE_TIMEOUT;

    @Value("${accounts.persistence.enabled}")
    private boolean isPersistenceEnabled;

    @Value("${proxy}")
    private String proxy;

    private final ExecutorService executor;
    private final BrowserCdpInitializer cdpInitializer;
    private final AccountCommandPort accountCommandPort;
    private final IdentityProtectionBrowserPort identityProtectionBrowserPort;

    @Override
    public void process(List<Account> accounts) {
        try {
            List<CompletableFuture<Optional<Account>>> futures = accounts.stream().map(this::processAsync).toList();
            List<Optional<Account>> completedAccounts = futures.stream().map(CompletableFuture::join).filter(Optional::isPresent).toList();
            finishProcess(completedAccounts);
        } finally {
            ExecutorUtils.shutDownGracefully(executor);
        }
    }

    private CompletableFuture<Optional<Account>> processAsync(Account account) {
        return CompletableFuture.supplyAsync(() -> process(account), executor);
    }

    private Optional<Account> process(Account account) {
        ProfileResponse profile = identityProtectionBrowserPort.createProfile(account.firstName(), proxy);
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

    private void navigateToFifa(Browser browser) {
        browser.navigate(BASE_URL);
    }

    private void goToLogIn(Browser browser) {
        browser.click(LOG_IN_BUTTON);
        browser.waitForSelector(QUEUE_BUTTON);
    }

    private void waitInQueue(Browser browser) {
        browser.click(QUEUE_BUTTON);
        browser.waitForSelector(EMAIL_INPUT, QUEUE_TIMEOUT);
    }

    private void processLogIn(Account account, Browser browser) {
        browser.fill(EMAIL_INPUT, account.email());
        browser.fill(PASSWORD_INPUT, account.password());
        browser.click(LOGIN_BUTTON);
        browser.waitForSelector(DROPS_CHECKBOX);
    }

    private void allowDrops(Account account, Browser browser) {
        if (browser.isVisible(DROPS_CHECKED)) {
            log.info("Account {} already has drops.", account.email());
            return;
        }

        browser.click(DROPS_CHECKBOX);
        browser.click(SAVE_BUTTON);
        browser.waitForSelector(SUCCESS_TITLE);
    }

    private void finishProcess(List<Optional<Account>> accounts) {
        if (!isPersistenceEnabled) return;
        accounts.forEach(account -> accountCommandPort.update(account.get().id(), Status.REGISTERED));
    }
}
