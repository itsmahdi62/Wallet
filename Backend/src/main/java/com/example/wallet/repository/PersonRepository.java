package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository <Person , Long> {
    @Query("select o from person o where o.deletedDate is null")
    List<Person> listOfExistingPeople();

    public Person findByNationalId(String nationalId);

    public Optional<Person> findById(Long id);
}
