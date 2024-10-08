package com.example.wallet.entity;

import com.example.wallet.entity.PersonEnums.MilitaryServiceStatus;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "person")
@Table(name = "person")
@SequenceGenerator(sequenceName = "server1_orcl_seq", name = "ps", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Person implements Serializable {
    @Id
    @GeneratedValue(generator = "ps")
    private Long id;

    @Column(name = "national_Id" , unique=true)
    @NotEmpty(message = "nationalId can not be empty !")
    @Pattern(regexp = "^\\d{10}$" ,  message = "Person national id must be 10 numbers !")
    private String nationalId;

    private String name;

    private String family;

    @Column(name = "phone_number" , unique=true)
    @NotEmpty(message = "personId can not be empty !")
    @Pattern(regexp = "^09\\d{9}$" , message = "Invalid input !")
    private String phoneNumber;

    private int dayOfBirth;
    private int monthOfBirth;
    @NotNull(message = "Year of birth must have been chosen")
    private int yearOfBirth;


    @NotNull(message = "Gender must be selected ")
    private Boolean isMale;

    @Enumerated(EnumType.STRING)  // Persisting Enum as String
    private MilitaryServiceStatus militaryServiceStatus;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Year of birth cannot be empty")
    private int age;
    // for logical delete
    private String deletedDate = null;
    /*
    I learned when a hacker wants to attack me , I should place some wrong information
    in my database and give him this wrong information to convince him it is enough for attacking ,you already have access to my db
     */
    private boolean isValid = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
}
