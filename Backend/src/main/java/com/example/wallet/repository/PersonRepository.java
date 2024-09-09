package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository <Person , String> {
}
