package com.example.demo.service;

import com.example.demo.dto.CityDTO;
import com.example.demo.model.City;
import com.example.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public City getById(Long id) {
        Optional<City> opt_city = cityRepository.findById(id);
        if(opt_city.isPresent()){
            return opt_city.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
    }

    public ResponseEntity<City> create(CityDTO new_city) {
        if(new_city.country_id() == null || new_city.city() == null || new_city.city().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City name and country cannot be empty");
        }

        City city = new City();
        city.setCity(new_city.city());
        city.setCountry_id(new_city.country_id());
        city.setLast_update(new Date());

        City saved = cityRepository.save(city);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<City> update(Long id, CityDTO dto) {
        Optional<City> opt_city = cityRepository.findById(id);
        if(opt_city.isPresent()){
            City city = opt_city.get();
            if(dto.country_id()!=null)
                // TODO sprawdzenie cyz istnieje
                city.setCountry_id(dto.country_id());
            if(dto.city()!=null && !dto.city().isEmpty()){
                city.setCity(dto.city());
            }
            city.setLast_update(new Date());
            cityRepository.save(city);
            return ResponseEntity.ok().body(city);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
        }
    }

    public ResponseEntity<City> delete(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if(city.isPresent()){
            cityRepository.delete(city.get());
            return ResponseEntity.ok().build();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
        }
    }
}
