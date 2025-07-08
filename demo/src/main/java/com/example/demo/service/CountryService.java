package com.example.demo.service;

import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CountryCreateDTO;
import com.example.demo.dto.CountryGetDTO;
import com.example.demo.dto.CountryUpdateDTO;
import com.example.demo.mapper.CountryMapper;
import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public Page<CountryGetDTO> getAll(Map<String, String> filter, Pageable pageable) {
        if  (filter.isEmpty())
            return countryRepository.findAll(pageable).map(countryMapper::toGetDTO);

        if (filter.size() > 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are allowed to filter only by one parameter");

        String key = filter.keySet().iterator().next();
        String value = filter.values().iterator().next();

        var method = getStringPageFunction(pageable, key);

        return method.apply(value).map(countryMapper::toGetDTO);
    }

    private Function<String, Page<Country>> getStringPageFunction(Pageable pageable, String key) {
        Map<String, Function<String, Page<Country>>> filterMethods = Map.of(
                "country", v -> countryRepository.findByCountry(v, pageable)
        );

        var method = filterMethods.get(key);

        if (method == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown filter parameter: " + key);
        }
        return method;
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
