package com.daniel.fifa_account_drops.adapter.account.out.persistence;

import com.daniel.fifa_account_drops.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostgresAccountRepository extends JpaRepository<PostgresAccount, Long> {
    List<PostgresAccount> findAllByStatus(Status status);
    PostgresAccount findFirstByStatus(Status status);
}
