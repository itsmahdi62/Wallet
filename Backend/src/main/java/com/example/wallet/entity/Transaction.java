package com.example.wallet.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name="account_transaction")
@Table(name="account_transaction")
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
    
    private LocalTime createdTime ;
    private LocalDate createdDate ;
    @Column(name="transaction_amount")
    @Min(100000L)
    private Long transactionAmount;

    private Long accountBalanceAfterTransaction;

    @JsonProperty("isDeposit")   // Ensures correct mapping
    private boolean isDeposit ;
    private String deletedDate;
}
