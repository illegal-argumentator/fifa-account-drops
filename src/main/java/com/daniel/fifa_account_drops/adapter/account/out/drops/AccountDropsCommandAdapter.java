package com.daniel.fifa_account_drops.adapter.account.out.drops;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.port.internal.AccountDropsCommandPort;
import com.daniel.fifa_account_drops.shared.ExecutorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
final class AccountDropsCommandAdapter implements AccountDropsCommandPort {

    private final ExecutorService executor;
    private final DropsFlowService dropsFlowService;

    @Override
    public void process(List<Account> accounts) {
        try {
            List<CompletableFuture<Optional<Account>>> futures = accounts.stream().map(this::processAsync).toList();
            futures.stream().map(CompletableFuture::join).forEach(dropsFlowService::finishProcess);
        } finally {
            ExecutorUtils.shutDownGracefully(executor);
        }
    }

    private CompletableFuture<Optional<Account>> processAsync(Account account) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing account: {}.", account.email());
            return dropsFlowService.process(account);
        }, executor);
    }


}
