package com.daniel.fifa_account_drops.adapter.http.out.okhttp.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient() {

        return new OkHttpClient().newBuilder()
                .callTimeout(Duration.ofSeconds(30))
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .build();
    }

}
