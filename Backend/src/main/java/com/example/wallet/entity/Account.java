package com.example.wallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Entity(name = "Account")
@Table(name = "Account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @Id
    private Long id;

    @Column(name="Account_Number" , unique = true)
    @NotEmpty(message = "Account Number can not be empty")
    @Pattern(regexp = "^\\d{13}$" ,  message = "Person national id must be 13 numbers !")
    private String accountNumber;

    @Min(1000)
    @Column(name="Account_Balance")
    private Long accountBalance = 15000L;

    @Pattern(regexp = "^IR\\d{24}$" ,  message = "Person national id must be 26 numbers !")
    private String shaba;
    private Date CreationDate;
}
