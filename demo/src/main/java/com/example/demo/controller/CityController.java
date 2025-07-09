package com.example.demo.controller;

import com.example.demo.annotations.ForCity;
import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CityUpdateDTO;
import com.example.demo.model.City;
import com.example.demo.service.CityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/city")
@Slf4j
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    public Page<CityGetDTO> getCites(@RequestParam(required = false) Map<String, String> params, @ForCity @PageableDefault(page = 0, size = 10, sort = "cityId") Pageable pageable){
        log.info("Received request to get all cities with params: {} and pageable: {}", params, pageable);
        return cityService.getAll(params, pageable);
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Integer id){
        log.info("Received request to get city with ID: {}", id);
        return cityService.getById(id);
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody CityCreateDTO city){
        log.info("Received request to create a new city: {}", city);
        return cityService.create(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Integer id, @Valid @RequestBody CityUpdateDTO city){
        log.info("Received request to update city with ID: {} with data: {}", id, city);
        return cityService.update(id, city);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCustomer(@PathVariable Integer id){
        log.info("Received request to delete city with ID: {}", id);
        return cityService.delete(id);
    }
}