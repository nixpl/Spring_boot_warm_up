package com.example.demo.repository;

import com.example.demo.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Page<Country> findByCountry(String country, Pageable pageable);
}
