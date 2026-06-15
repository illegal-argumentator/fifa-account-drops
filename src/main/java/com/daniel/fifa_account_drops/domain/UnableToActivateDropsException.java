package com.daniel.fifa_account_drops.domain;

public class UnableToActivateDropsException extends RuntimeException {
    public UnableToActivateDropsException(String message) {
        super(message);
    }
}
