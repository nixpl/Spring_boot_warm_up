package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.model.Inventory;
import com.example.demo.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer>, JpaSpecificationExecutor<Rental> {

    List<Rental> findByInventory(Inventory inventory);
    List<Rental> findByCustomer(Customer customer);
    List<Rental> findByRentalDateBefore(Date date);
}
