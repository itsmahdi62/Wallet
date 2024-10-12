package com.example.wallet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "account")
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name="account_number" , unique = true)
    @NotEmpty(message = "Account Number can not be empty")
    @Pattern(regexp = "^\\d{13}$" ,  message = "Account number id must be 13 numbers !")
    private String accountNumber;

    @Column(name="account_balance")
    private Long accountBalance;

    @Pattern(regexp = "^IR\\d{24}$" ,  message = "Person national id must be 26 numbers !")
    private String shaba;

    @Column(name = "creation_date")  // Explicit naming to avoid issues
    private LocalDate creationDate;

    @Column(name = "deleted_date", nullable = true)
    private String deletedDate = null;

    @Max(10000000)
    private Long dailyWithdrawAmount = 0L;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_transaction_id",  referencedColumnName = "id")
    private List<Transaction> accountTransactionList ;

}
