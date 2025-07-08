package com.example.demo.repository;

import com.example.demo.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
    Page<City> findByCity(String city, Pageable pageable);

    Page<City> findByCountry_Country(String country, Pageable pageable);
}
