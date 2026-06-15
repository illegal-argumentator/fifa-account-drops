package com.daniel.fifa_account_drops.domain;

import java.time.LocalDate;

public record Account(

        long id,
        String firstName,
        String lastName,
        String countryCode,
        LocalDate dateOfBirth,
        String email,
        String password,
        Status status

) {
}
