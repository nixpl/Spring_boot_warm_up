package com.example.demo.service;

import com.example.demo.exception.TooManyFiltersException;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.specification.CountrySpecifications;
import com.example.demo.dto.CountryCreateDTO;
import com.example.demo.dto.CountryGetDTO;
import com.example.demo.dto.CountryUpdateDTO;
import com.example.demo.mapper.CountryMapper;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public Page<CountryGetDTO> getAll(Map<String, String> params, Pageable pageable) {
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<Country> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        filterParams.remove("page");
        filterParams.remove("size");
        filterParams.remove("sort");
        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            spec = spec.and(CountrySpecifications.hasSearchTerm(searchTerm));
        }

        if (filterParams.size() > 1) {
            throw new TooManyFiltersException(filterParams.keySet());
        }

        if (!filterParams.isEmpty()) {
            Map.Entry<String, String> entry = filterParams.entrySet().iterator().next();
            spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
        }

        return countryRepository.findAll(spec, pageable).map(countryMapper::toGetDTO);
    }

    private Specification<Country> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "country" -> (root, query, cb) -> cb.equal(root.get("country"), value);
            default -> throw new UnknownFilterParameterException(key);
        };
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
