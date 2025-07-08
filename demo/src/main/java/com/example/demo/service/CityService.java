package com.example.demo.service;

import com.example.demo.specification.CitySpecifications;
import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CityUpdateDTO;
import com.example.demo.mapper.CityMapper;
import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
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
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<City> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            spec = spec.and(CitySpecifications.hasSearchTerm(searchTerm));
        }

        if (filterParams.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You are allowed to use the 'search' parameter with at most one other filter.");
        }

        if (!filterParams.isEmpty()) {
            Map.Entry<String, String> entry = filterParams.entrySet().iterator().next();
            spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
        }

        return cityRepository.findAll(spec, pageable).map(cityMapper::toGetDTO);
    }

    private Specification<City> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "city" -> (root, query, cb) -> cb.equal(root.get("city"), value);
            case "country" -> (root, query, cb) -> cb.equal(root.get("country").get("country"), value);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown filter parameter: " + key);
        };
    }


    public City getById(Integer id) {
        return cityRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("cityId"));
    }

    public ResponseEntity<City> create(CityCreateDTO newCity) {
        if(newCity.countryId() == null || newCity.city() == null || newCity.city().isEmpty()){
            throw new DataIntegrityViolationException("City name and country cannot be empty");
        }

        City city = new City();
        city.setCity(newCity.city());
        Country country = countryRepository.findById(newCity.countryId()).orElseThrow(() -> new EntityNotFoundException("countryId"));
        city.setCountry(country);
        city.setLastUpdate(new Date());

        City saved = cityRepository.save(city);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<City> update(Integer  id, CityUpdateDTO dto) {

        City city = cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cityId"));
        Country country = countryRepository.findById(dto.countryId()).orElseThrow(() -> new EntityNotFoundException("countryId"));

        if(dto.city() != null)
            city.setCity(dto.city());

        city.setCountry(country);

        city.setLastUpdate(new Date());
        City saved = cityRepository.save(city);
        return ResponseEntity.ok().body(saved);
    }

    public ResponseEntity<City> delete(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if(city.isPresent()){
            cityRepository.delete(city.get());
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("cityId");
        }
    }
}
