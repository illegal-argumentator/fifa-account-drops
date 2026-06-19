package com.daniel.fifa_account_drops.adapter.account.out.mapper;

import com.daniel.fifa_account_drops.adapter.account.out.persistence.PostgresAccount;
import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface AccountMapper {

    List<Account> toAccounts(List<PostgresAccount> entities);
    Account toAccount(PostgresAccount entity);

}
