package com.daniel.fifa_account_drops.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
class ExecutorConfig {

    @Value("${threads.count}")
    private int count;

    @Bean
    public ExecutorService executorService() {
        int threads = count;
        if (count < 1) threads = Runtime.getRuntime().availableProcessors();

        log.info("Running application with {} threads.", threads);
        return Executors.newFixedThreadPool(threads);
    }

}
