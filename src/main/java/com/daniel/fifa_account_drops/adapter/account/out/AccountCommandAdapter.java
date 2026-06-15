package com.daniel.fifa_account_drops.adapter.account.out;

import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccount;
import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccountRepository;
import com.daniel.fifa_account_drops.domain.AccountNotFoundException;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.AccountCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
final class AccountCommandAdapter implements AccountCommandPort {

    private final PostgresAccountRepository repository;

    @Override
    public void update(long id, Status status) {
        PostgresAccount entity = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found by id: %d.".formatted(id)));

        entity.setStatus(status);
        repository.save(entity);
    }
}
