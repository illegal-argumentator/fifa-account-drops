package com.daniel.fifa_account_drops.application.external;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.external.AccountDropCommandPort;
import com.daniel.fifa_account_drops.port.internal.AccountCommandPort;
import com.daniel.fifa_account_drops.port.external.AccountDropUseCase;
import com.daniel.fifa_account_drops.port.external.ExternalAccountQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Profile("user")
@RequiredArgsConstructor
public class AccountDropService implements AccountDropUseCase {

    private final ExternalAccountQueryPort externalAccountQueryPort;
    private final AccountDropCommandPort accountDropCommandPort;
    private final AccountCommandPort accountCommandPort;

    @Override
    public void process() {
        Account account = externalAccountQueryPort.retrieve();
        if (account == null) return;

        try {
            accountDropCommandPort.process(account);
        } catch (Exception e) {
            log.error(e.getMessage());
            accountCommandPort.update(account.id(), Status.PENDING);
        }

        process();
    }
}
