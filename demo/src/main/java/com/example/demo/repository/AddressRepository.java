package com.example.demo.repository;

import com.example.demo.model.Address;
import com.example.demo.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Page<Address> findByAddress(String address, Pageable pageable);

    Page<Address> findByAddress2(String address2, Pageable pageable);

    Page<Address> findByDistrict(String district, Pageable pageable);

    Page<Address> findByCity_City(String city, Pageable pageable);

    Page<Address> findByCity_Country_Country(String country, Pageable pageable);
}

