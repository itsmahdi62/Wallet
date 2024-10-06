package com.example.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name="transaction")
@Table(name="transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // private LocalDateTime
    private LocalTime time ;
    private LocalDate date ;
    @Column(name="transaction_amount")
    @Min(100000)
    private Long transactionAmount;

    private Long accountBalanceAfterTransaction;

    // this could be enumeration
    private boolean isDeposit ;
}
