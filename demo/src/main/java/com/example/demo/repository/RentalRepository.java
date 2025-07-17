package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.model.Inventory;
import com.example.demo.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer>, JpaSpecificationExecutor<Rental> {

    List<Rental> findByInventory(Inventory inventory);
    List<Rental> findByCustomer(Customer customer);
    List<Rental> findByRentalDateBefore(Date date);

    @Query("SELECT r FROM Rental r JOIN FETCH r.inventory i JOIN FETCH i.film f WHERE r.rentalDate < :marginDate AND r.returnDate IS NULL")
    List<Rental> findDelayedRentals(@Param("marginDate") Date marginDate);

    Optional<Rental> findFirstByCustomerOrderByRentalDateDesc(Customer customer);
}
