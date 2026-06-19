package com.daniel.fifa_account_drops.adapter.account.in.internal;

import com.daniel.fifa_account_drops.port.internal.AccountDropsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("server")
@RequiredArgsConstructor
final class AccountDropsStarter implements ApplicationRunner {

    private final AccountDropsUseCase useCase;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting account drops process.");
        useCase.process();
        log.info("Finished processing.");
    }
}
