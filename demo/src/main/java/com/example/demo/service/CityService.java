package com.example.demo.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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

    public Page<CityGetDTO> getAll(Map<String, String> filter, Pageable pageable) {
        if  (filter.isEmpty())
            return cityRepository.findAll(pageable).map(cityMapper::toGetDTO);

        if (filter.size() > 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are allowed to filter only by one parameter");

        String key = filter.keySet().iterator().next();
        String value = filter.values().iterator().next();

        var method = getStringPageFunction(pageable, key);

        return method.apply(value).map(cityMapper::toGetDTO);
    }

    private Function<String, Page<City>> getStringPageFunction(Pageable pageable, String key) {
        Map<String, Function<String, Page<City>>> filterMethods = Map.of(
                "city", v -> cityRepository.findByCity(v, pageable),
                "country", v -> cityRepository.findByCountry_Country(v, pageable)
        );

        var method = filterMethods.get(key);

        if (method == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown filter parameter: " + key);
        }
        return method;
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
