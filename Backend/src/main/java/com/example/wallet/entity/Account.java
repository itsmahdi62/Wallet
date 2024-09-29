package com.example.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "Account")
@Table(name = "Account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

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
}
