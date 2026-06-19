package com.daniel.fifa_account_drops.port.internal;

import com.daniel.fifa_account_drops.domain.Account;

import java.util.List;

public interface AccountDropsCommandPort {

    void process(List<Account> accounts);

}
