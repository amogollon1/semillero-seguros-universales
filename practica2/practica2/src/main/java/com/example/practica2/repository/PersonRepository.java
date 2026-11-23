package com.example.practica2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practica2.entity.Person;

@Repository("personRepository")
public interface PersonRepository extends JpaRepository<Person, String>{

	Optional<Person> findByUserName(String userName);
}
