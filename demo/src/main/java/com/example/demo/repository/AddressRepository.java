package com.example.demo.repository;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.model.City;
import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByPhone(String phone);

    Optional<Address> findByAddressAndAddress2AndDistrictAndCityAndPostalCodeAndPhone(
            String address,
            String address2,
            String district,
            City city,
            String postalCode,
            String phone
    );
}
