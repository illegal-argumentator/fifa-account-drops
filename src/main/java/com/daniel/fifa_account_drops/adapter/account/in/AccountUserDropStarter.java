package com.daniel.fifa_account_drops.adapter.account.in;

import com.daniel.fifa_account_drops.port.AccountDropUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("user")
@RequiredArgsConstructor
public class AccountUserDropStarter implements ApplicationRunner {

    private final AccountDropUseCase accountDropUseCase;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starter user drop process.");
        accountDropUseCase.process();
        log.info("Successfully finished account process.");
    }
}
