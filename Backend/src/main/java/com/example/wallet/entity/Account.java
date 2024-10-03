package com.example.wallet.entity;

import jakarta.persistence.*;
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

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Person person;

    @OneToMany(mappedBy = "account")
    private List<Transaction> accountTransactionList ;

    @Column(name="Account_Number" , unique = true)
    @NotEmpty(message = "Account Number can not be empty")
    @Pattern(regexp = "^\\d{13}$" ,  message = "Account number id must be 13 numbers !")
    private String accountNumber;

    @Min(1000)
    @Column(name="Account_Balance")
    private Long accountBalance;

    @Pattern(regexp = "^IR\\d{24}$" ,  message = "Person national id must be 26 numbers !")
    private String shaba;
    private LocalDate CreationDate;

    private String deletedDate = null;
}
