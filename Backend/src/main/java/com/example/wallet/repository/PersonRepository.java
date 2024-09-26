package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PersonRepository extends JpaRepository <Person , Long> {
}
