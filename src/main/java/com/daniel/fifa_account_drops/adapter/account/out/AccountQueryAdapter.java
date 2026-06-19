package com.daniel.fifa_account_drops.adapter.account.out;

import com.daniel.fifa_account_drops.adapter.account.out.mapper.AccountMapper;
import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccount;
import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccountRepository;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.internal.AccountQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
final class AccountQueryAdapter implements AccountQueryPort {

    private final AccountMapper mapper;
    private final PostgresAccountRepository repository;

    @Override
    public List<Account> getAllBy(Status status) {
        List<PostgresAccount> entities = repository.findAllByStatus(status);
        return mapper.toAccounts(entities);
    }

}
