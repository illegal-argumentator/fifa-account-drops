package com.daniel.fifa_account_drops.adapter.account.out.drops;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.external.AccountDropCommandPort;
import com.daniel.fifa_account_drops.port.internal.AccountCommandPort;
import com.daniel.fifa_account_drops.shared.WaitUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDropCommandAdapter implements AccountDropCommandPort {

    private final DropsFlowService dropsFlowService;

    private final AccountCommandPort accountCommandPort;

    @Override
    public void process(Account account) {
        log.info("Processing account: {}.", account.email());
        Optional<Account> process = dropsFlowService.process(account);

        if (process.isEmpty()) {
            accountCommandPort.update(account.id(), Status.PENDING);
            log.info("Could not proceed account. Sleeping for 10 minutes.");
            WaitUtils.waitSafely(60 * 10);
        } else {
            dropsFlowService.finishProcess(process);
        }
    }
}
