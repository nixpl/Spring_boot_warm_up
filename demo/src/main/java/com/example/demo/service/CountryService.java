package com.example.demo.service;

import com.example.demo.dto.CountryDTO;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Country getById(Integer  id) {
        return countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("city_id"));
    }

    public ResponseEntity<Country> create(CountryDTO new_country) {
        Country country = new Country();
        country.setCountry(new_country.country());
        country.setLast_update(new Date());
        countryRepository.save(country);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    public ResponseEntity<Country> update(Integer  id, CountryDTO dto) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("country_id"));
        country.setCountry(dto.country());
        countryRepository.save(country);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Country> delete(Integer  id) {
        Optional<Country> country = countryRepository.findById(id);
        if(country.isPresent()){
            countryRepository.delete(country.get());
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("country_id");
        }
    }
}
