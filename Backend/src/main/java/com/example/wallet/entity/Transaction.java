package com.example.wallet.entity;

import jakarta.persistence.*;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;
    // private LocalDateTime
    private LocalTime time ;
    private LocalDate date ;

    @Column(name="transaction_amount")
    private Long transactionAmount;
    private Long accountBalanceAfterTransaction;
    private Long dailyTransactionAmount ;

}
