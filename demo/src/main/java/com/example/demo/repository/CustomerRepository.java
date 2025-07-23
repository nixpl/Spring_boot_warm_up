package com.example.demo.repository;

import com.example.demo.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    Page<Customer> findByFirstName(String firstName, Pageable pageable);

    Page<Customer> findByLastName(String lastName, Pageable pageable);

    Page<Customer> findByEmail(String email, Pageable pageable);

    Page<Customer> findByActive(Integer active, Pageable pageable);

    Page<Customer> findByAddress_Address(String address, Pageable pageable);

    Page<Customer> findByAddress_District(String district, Pageable pageable);

    Page<Customer> findByAddress_City_City(String city, Pageable pageable);

    Page<Customer> findByAddress_City_Country_Country(String country, Pageable pageable);





    Optional<Customer> findByEmail(String email);

    @Query("SELECT c.customerId FROM Customer c WHERE c.activebool = true " +
            "AND NOT EXISTS (SELECT r FROM Rental r WHERE r.customer = c AND r.returnDate IS NULL) " +
            "AND c.customerId IN (SELECT r2.customer.customerId FROM Rental r2 WHERE r2.customer = c GROUP BY r2.customer.customerId HAVING MAX(r2.rentalDate) < :nMonthsAgo)")
    List<Integer> findCustomerIdsToDeactivate(@Param("nMonthsAgo") Date nMonthsAgo);

    @Modifying
    @Query("UPDATE Customer c SET c.activebool = false WHERE c.customerId IN (:customerIds)")
    int deactivateCustomers(@Param("customerIds") List<Integer> customerIds);

}
