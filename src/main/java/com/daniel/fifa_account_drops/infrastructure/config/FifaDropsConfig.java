package com.daniel.fifa_account_drops.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FifaDropsConfig {

    @Value("${fifa.drops.limit}")
    private int limit;

    @Value("${fifa.drops.startFrom}")
    private int startFrom;

    @Bean
    public int startFrom() {
        return startFrom;
    }

    @Bean
    public int limit() {
        return limit;
    }

}
