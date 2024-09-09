package com.example.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity(name = "person")
@Table(name = "person")
@SequenceGenerator(sequenceName = "server1_orcl_seq", name = "ps", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Person {
    @Id
    @GeneratedValue(generator = "ps")
    private Long id;
    @Column(name = "person_Id" , unique=true)
    @NotEmpty(message = "personId can not be empty !")
    @Pattern(regexp = "^/d{10}$" ,  message = "Person national id must be 10 numbers !")
    private String personId;
    private String name;
    private String family;
    @Column(name = "phone_number" , unique=true)
    @NotEmpty(message = "personId can not be empty !")
    @Pattern(regexp = "^09\\d{9}$" , message = "Invalid input !")
    private String phoneNumber;
    private String dateOfBirth;
    private Boolean isMail;
    @Enumerated(EnumType.STRING)  // Persisting Enum as String
    private MilitaryServiceStatus militaryServiceStatus;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
