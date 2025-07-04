package com.example.demo.service;

import com.example.demo.dto.CityDTO;
import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public City getById(Integer id) {
        return cityRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("city_id"));

//        Optional<City> opt_city = cityRepository.findById(id);
//        if(opt_city.isPresent()){
//            return opt_city.get();
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
    }

    public ResponseEntity<City> create(CityDTO new_city) {
        if(new_city.country_id() == null || new_city.city() == null || new_city.city().isEmpty()){
            throw new DataIntegrityViolationException("City name and country cannot be empty");
        }

        City city = new City();
        city.setCity(new_city.city());
        Country country = countryRepository.findById(new_city.country_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong country_id"));
        city.setCountry(country);

//        Optional<Country> country = countryRepository.findById(new_city.country_id());
//        if(country.isPresent()){
//            city.setCountry(country.get());
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong country_id");
//        }

        city.setLast_update(new Date());

        cityRepository.save(city);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    public ResponseEntity<City> update(Integer  id, CityDTO dto) {

        City city = cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("city_id"));
        Country country = countryRepository.findById(dto.country_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong country_id"));
        city.setCountry(country);
        city.setLast_update(new Date());
        cityRepository.save(city);
        return ResponseEntity.ok().build();

//        Optional<City> opt_city = cityRepository.findById(id);
//        if(opt_city.isPresent()){
//            City city = opt_city.get();
//            Optional<Country> country = countryRepository.findById(dto.country_id());
//            if(country.isPresent()){
//                city.setCountry(country.get());
//            }
//            else{
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong country_id");
//            }
//            if(dto.city()!=null && !dto.city().isEmpty()){
//                city.setCity(dto.city());
//            }
//            city.setLast_update(new Date());
//            cityRepository.save(city);
//            return ResponseEntity.ok().body(city);
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
//        }
    }

    public ResponseEntity<City> delete(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if(city.isPresent()){
            cityRepository.delete(city.get());
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("city_id");
        }
    }
}
