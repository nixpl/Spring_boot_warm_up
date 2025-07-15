package com.example.demo.service;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.info.ExceptionInfo;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.specification.CountrySpecifications;
import com.example.demo.dto.CountryCreateDTO;
import com.example.demo.dto.CountryGetDTO;
import com.example.demo.dto.CountryUpdateDTO;
import com.example.demo.mapper.CountryMapper;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public Page<CountryGetDTO> getAll(Map<String, String> params, Pageable pageable) {
        log.info("Attempting to retrieve all countries with parameters: {} and pageable: {}", params, pageable);
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<Country> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        filterParams.remove("page");
        filterParams.remove("size");
        filterParams.remove("sort");
        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            log.info("Applying search term specification: {}", searchTerm);
            spec = spec.and(CountrySpecifications.hasSearchTerm(searchTerm));
        }

        if (!filterParams.isEmpty()) {
            for (Map.Entry<String, String> entry : filterParams.entrySet()){
                log.info("Applying filter specification with key: '{}' and value: '{}'", entry.getKey(), entry.getValue());
                spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
            }
        }

        Page<CountryGetDTO> result = countryRepository.findAll(spec, pageable).map(countryMapper::toGetDTO);
        log.info("Successfully retrieved {} countries on page {}", result.getNumberOfElements(), pageable.getPageNumber());
        return result;
    }

    private Specification<Country> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "country" -> (root, query, cb) -> cb.equal(root.get("country"), value);
            default -> throw new UnknownFilterParameterException(ExceptionInfo.UNKNOWN_COUNTRY_FILTER_PARAMETER, key);
        };
    }

    public Country getById(Integer  id) {
        log.info("Attempting to retrieve country with ID: {}", id);
        Country country = countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_COUNTRY_NOT_FOUND, id));
        log.info("Successfully retrieved country with ID: {}", id);
        return country;
    }

    public ResponseEntity<Country> create(CountryCreateDTO newCountry) {
        log.info("Attempting to create a new country with DTO: {}", newCountry);
        Country country = new Country();
        country.setCountry(newCountry.country());
        country.setLastUpdate(new Date());
        Country saved = countryRepository.save(country);
        log.info("Successfully created and saved country with ID: {}", saved.getCountryId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<Country> update(Integer  id, CountryUpdateDTO dto) {
        log.info("Attempting to update country with ID: {} using DTO: {}", id, dto);
        Country country = countryRepository.findById(id).orElseThrow(() -> new  EntityNotFoundException(ExceptionInfo.ENTITY_COUNTRY_NOT_FOUND, id));
        country.setCountry(dto.country());
        Country saved = countryRepository.save(country);
        log.info("Successfully updated country with ID: {}", saved.getCountryId());
        return ResponseEntity.ok().body(saved);
    }

    public ResponseEntity<Country> delete(Integer  id) {
        log.info("Attempting to delete country with ID: {}", id);
        Optional<Country> country = countryRepository.findById(id);
        if(country.isPresent()){
            countryRepository.delete(country.get());
            log.info("Successfully deleted country with ID: {}", id);
            return ResponseEntity.ok().build();
        }
        else{
            throw new  EntityNotFoundException(ExceptionInfo.ENTITY_COUNTRY_NOT_FOUND, id);
        }
    }
}