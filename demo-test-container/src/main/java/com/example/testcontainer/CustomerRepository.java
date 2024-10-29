package com.example.testcontainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Iterable<Customer> findByName(String name);
}
