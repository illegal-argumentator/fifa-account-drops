package com.daniel.fifa_account_drops.application;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.AccountCommandPort;
import com.daniel.fifa_account_drops.port.AccountDropUseCase;
import com.daniel.fifa_account_drops.port.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.port.ExternalAccountQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDropService implements AccountDropUseCase {

    private final ExternalAccountQueryPort externalAccountQueryPort;
    private final AccountDropsCommandPort accountDropsCommandPort;
    private final AccountCommandPort accountCommandPort;

    @Override
    public void process() {
        Account account = externalAccountQueryPort.retrieve();
        if (account == null) return;
        log.info("Processing account: {}.", account.email());

        try {
            accountDropsCommandPort.process(List.of(account));
        } catch (Exception e) {
            log.error(e.getMessage());
            accountCommandPort.update(account.id(), Status.PENDING);
        }

        process();
    }
}
