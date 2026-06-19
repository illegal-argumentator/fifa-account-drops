package com.daniel.fifa_account_drops.application.external;

import com.daniel.fifa_account_drops.adapter.account.out.mapper.AccountMapper;
import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccount;
import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccountRepository;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;
import com.daniel.fifa_account_drops.port.external.AccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final Lock lock = new ReentrantLock();

    private final PostgresAccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public Account pick() {
        lock.lock();

        try {
            PostgresAccount entity = repository.findFirstByStatus(Status.PENDING);

            entity.setStatus(Status.IN_PROGRESS);
            repository.save(entity);

            return mapper.toAccount(entity);
        } finally {
            lock.unlock();
        }
    }
}
