package com.example.demo.service;

import com.example.demo.dto.CountryCreateDTO;
import com.example.demo.dto.CountryUpdateDTO;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Page<Country> getAll(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country getById(Integer  id) {
        return countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cityId"));
    }

    public ResponseEntity<Country> create(CountryCreateDTO newCountry) {
        Country country = new Country();
        country.setCountry(newCountry.country());
        country.setLastUpdate(new Date());
        Country saved = countryRepository.save(country);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<Country> update(Integer  id, CountryUpdateDTO dto) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("countryId"));
        country.setCountry(dto.country());
        Country saved = countryRepository.save(country);
        return ResponseEntity.ok().body(saved);
    }

    public ResponseEntity<Country> delete(Integer  id) {
        Optional<Country> country = countryRepository.findById(id);
        if(country.isPresent()){
            countryRepository.delete(country.get());
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("countryId");
        }
    }
}
