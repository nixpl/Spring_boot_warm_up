package com.example.demo.controller;

import com.example.demo.dto.country.CountryCreateDTO;
import com.example.demo.dto.country.CountryGetDTO;
import com.example.demo.dto.country.CountryUpdateDTO;
import com.example.demo.model.Country;
import com.example.demo.service.CountryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/country")
@Slf4j
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public Page<CountryGetDTO> getCountries(
            @RequestParam(required = false) Map<String, String> params,
            @PageableDefault(page = 0, size = 10, sort = "countryId") Pageable pageable) {
        log.info("Received request to get all countries with params: {} and pageable: {}", params, pageable);
        return countryService.getAll(params, pageable);
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Integer id) {
        log.info("Received request to get country with ID: {}", id);
        return countryService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@Valid @RequestBody CountryCreateDTO country) {
        log.info("Received request to create a new country: {}", country);
        return countryService.create(country);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Integer id, @Valid @RequestBody CountryUpdateDTO country) {
        log.info("Received request to update country with ID: {} with data: {}", id, country);
        return countryService.update(id, country);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Country> deleteCountry(@PathVariable Integer id) {
        log.info("Received request to delete country with ID: {}", id);
        return countryService.delete(id);
    }
}