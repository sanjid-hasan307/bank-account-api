package com.example.demo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "account_holders")
@Data
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String accountNumber;
    private String nid;
    private String phoneNumber;
    private Double balance;
}
