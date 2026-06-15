package com.daniel.fifa_account_drops.adapter.account.out.persistence;

import com.daniel.fifa_account_drops.domain.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "accounts")
public class PostgresAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String firstName;
    private String lastName;

    private String countryCode;
    private LocalDate dateOfBirth;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;

}
