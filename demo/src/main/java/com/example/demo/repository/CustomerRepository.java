package com.example.demo.repository;

import com.example.demo.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Page<Customer> findByFirstName(String firstName, Pageable pageable);

    Page<Customer> findByLastName(String lastName, Pageable pageable);

    Page<Customer> findByEmail(String email, Pageable pageable);

    Page<Customer> findByActive(Integer active, Pageable pageable);

    Page<Customer> findByAddress_Address(String address, Pageable pageable);

    Page<Customer> findByAddress_City_City(String city, Pageable pageable);

    Page<Customer> findByAddress_City_Country(String country, Pageable pageable);



    

    Optional<Customer> findByEmail(String email);


}
