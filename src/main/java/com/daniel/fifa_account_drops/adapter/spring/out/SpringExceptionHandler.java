package com.daniel.fifa_account_drops.adapter.spring.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SpringExceptionHandler {

    @ExceptionHandler({NullPointerException.class, IllegalStateException.class, IllegalArgumentException.class})
    public void handleExceptions(Exception e) {
        log.error("Handling internal server exception: {}.", e.getMessage());
    }

}
