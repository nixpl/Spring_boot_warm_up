package com.example.demo.service;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.info.ExceptionInfo;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.specification.CitySpecifications;
import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CityUpdateDTO;
import com.example.demo.mapper.CityMapper;
import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.repository.CityRepository;
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
public class CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.cityMapper = cityMapper;
    }

    public Page<CityGetDTO> getAll(Map<String, String> params, Pageable pageable) {
        log.info("Attempting to retrieve all cities with parameters: {} and pageable: {}", params, pageable);
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<City> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        filterParams.remove("page");
        filterParams.remove("size");
        filterParams.remove("sort");
        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            log.info("Applying search term specification: {}", searchTerm);
            spec = spec.and(CitySpecifications.hasSearchTerm(searchTerm));
        }

        if (!filterParams.isEmpty()) {
            for (Map.Entry<String, String> entry : filterParams.entrySet()){
                log.info("Applying filter specification with key: '{}' and value: '{}'", entry.getKey(), entry.getValue());
                spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
            }
        }

        Page<CityGetDTO> result = cityRepository.findAll(spec, pageable).map(cityMapper::toGetDTO);
        log.info("Successfully retrieved {} cities on page {}", result.getNumberOfElements(), pageable.getPageNumber());
        return result;
    }

    private Specification<City> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "city" -> (root, query, cb) -> cb.equal(root.get("city"), value);
            case "country" -> (root, query, cb) -> cb.equal(root.get("country").get("country"), value);
            default -> throw new UnknownFilterParameterException(ExceptionInfo.UNKNOWN_CITY_FILTER_PARAMETER, key);
        };
    }


    public City getById(Integer id) {
        log.info("Attempting to retrieve city with ID: {}", id);
        City city = cityRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException(ExceptionInfo.ENTITY_CITY_NOT_FOUND, id));
        log.info("Successfully retrieved city with ID: {}", id);
        return city;
    }

    public ResponseEntity<City> create(CityCreateDTO newCity) {
        log.info("Attempting to create a new city with DTO: {}", newCity);

//        Chyba niepotrzebne
//        if(newCity.countryId() == null || newCity.city() == null || newCity.city().isEmpty()){
//            throw new DataIntegrityViolationException("City name and country cannot be empty");
//        }

        City city = new City();
        city.setCity(newCity.city());
        Country country = countryRepository.findById(newCity.countryId()).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_COUNTRY_NOT_FOUND, newCity.countryId()));
        city.setCountry(country);
        city.setLastUpdate(new Date());

        City saved = cityRepository.save(city);
        log.info("Successfully created and saved city with ID: {}", saved.getCityId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<City> update(Integer  id, CityUpdateDTO dto) {
        log.info("Attempting to update city with ID: {} using DTO: {}", id, dto);
        City city = cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_CITY_NOT_FOUND, id));
        Country country = countryRepository.findById(dto.countryId()).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_COUNTRY_NOT_FOUND, dto.countryId()));

        if(dto.city() != null)
            city.setCity(dto.city());

        city.setCountry(country);

        city.setLastUpdate(new Date());
        City saved = cityRepository.save(city);
        log.info("Successfully updated city with ID: {}", saved.getCityId());
        return ResponseEntity.ok().body(saved);
    }

    public ResponseEntity<City> delete(Integer id) {
        log.info("Attempting to delete city with ID: {}", id);
        Optional<City> city = cityRepository.findById(id);
        if(city.isPresent()){
            cityRepository.delete(city.get());
            log.info("Successfully deleted city with ID: {}", id);
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException(ExceptionInfo.ENTITY_CITY_NOT_FOUND, id);
        }
    }
}