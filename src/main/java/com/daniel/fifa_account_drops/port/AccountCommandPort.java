package com.daniel.fifa_account_drops.port;

import com.daniel.fifa_account_drops.domain.Status;

public interface AccountCommandPort {

    void update(long id, Status status);

}
