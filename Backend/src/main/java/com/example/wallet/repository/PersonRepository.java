package com.example.wallet.repository;

import com.example.wallet.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PersonRepository extends JpaRepository <Person , Long> {
    @Query("select o from person o where o.deletedDate is null")
    List<Person> listOfExistingPeople();

}
