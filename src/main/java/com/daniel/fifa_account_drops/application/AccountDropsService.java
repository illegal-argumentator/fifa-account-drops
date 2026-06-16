package com.daniel.fifa_account_drops.application;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.port.AccountDropsUseCase;
import com.daniel.fifa_account_drops.port.AccountQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
final class AccountDropsService implements AccountDropsUseCase {

    private final AccountQueryPort accountQueryPort;
    private final AccountDropsCommandPort accountDropsCommandPort;

    @Override
    public void process() {
        List<Account> pendingAccounts = accountQueryPort.getAllBy(Status.PENDING);
        log.info("Found {} accounts.", pendingAccounts.size());

        if (pendingAccounts.isEmpty()) return;
        accountDropsCommandPort.process(pendingAccounts);
    }
}
