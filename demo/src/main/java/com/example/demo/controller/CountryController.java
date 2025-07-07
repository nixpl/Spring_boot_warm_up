package com.example.demo.controller;

import com.example.demo.dto.CountryDTO;
import com.example.demo.dto.CountryUpdateDTO;
import com.example.demo.model.Country;
import com.example.demo.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public Page<Country> getCountries(Pageable pageable){
        return countryService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Integer id){
        return countryService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@Valid @RequestBody CountryDTO country){
        return countryService.create(country);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Integer  id, @Valid @RequestBody CountryUpdateDTO country){
        return countryService.update(id, country);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Country> deleteCountry(@PathVariable Integer  id){
        return countryService.delete(id);
    }
}
