package com.daniel.fifa_account_drops.adapter.account.out.drops;

import com.daniel.fifa_account_drops.adapter.browser.out.Browser;
import com.daniel.fifa_account_drops.adapter.browser.out.BrowserInitializer;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.port.AccountCommandPort;
import com.daniel.fifa_account_drops.port.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.shared.ExecutorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final BrowserInitializer initializer;

    private final ExecutorService executor;
    private final AccountCommandPort accountCommandPort;

    @Override
    public void process(List<Account> accounts, boolean immediateClose) {
        List<CompletableFuture<Optional<Account>>> futures = accounts.stream()
                .map(account -> CompletableFuture.supplyAsync(() -> process(account), executor))
                .toList();

        futures.stream()
                .map(CompletableFuture::join)
                .filter(Optional::isPresent);
//                .forEach(account -> accountCommandPort.update(account.get().id(), Status.REGISTERED));

        if (immediateClose) ExecutorUtils.shutDownGracefully(executor);
    }

    private Optional<Account> process(Account account) {
        try (Browser browser = initializer.init()) {

            navigateToFifa(browser);
            goToLogIn(browser);
            waitInQueue(browser);
            processLogIn(browser);
            allowDrops(browser);
            waitForSuccess(browser);

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
    }

    private void waitInQueue(Browser browser) {

    }

    private void processLogIn(Browser browser) {

    }

    private void allowDrops(Browser browser) {

    }

    private void waitForSuccess(Browser browser) {

    }
}
