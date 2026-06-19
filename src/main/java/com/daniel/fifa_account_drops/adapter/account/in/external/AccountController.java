package com.daniel.fifa_account_drops.adapter.account.in.external;

import com.daniel.fifa_account_drops.domain.Account;
import com.daniel.fifa_account_drops.port.external.AccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase useCase;

    @GetMapping("/pick")
    public ResponseEntity<Account> pick() {
        return ResponseEntity.ok(useCase.pick());
    }

}
