package com.daniel.fifa_account_drops.port.internal;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.domain.Status;

import java.util.List;

public interface AccountQueryPort {

    List<Account> getAllBy(Status status);


}
