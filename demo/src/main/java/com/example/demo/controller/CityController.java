package com.example.demo.controller;

import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CityUpdateDTO;
import com.example.demo.model.City;
import com.example.demo.service.CityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    public Page<CityGetDTO> getCites(@PageableDefault(page = 0, size = 10, sort = "cityId") Pageable pageable){
        return cityService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Integer  id){
        return cityService.getById(id);
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody CityCreateDTO city){
        return cityService.create(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Integer  id, @Valid @RequestBody CityUpdateDTO city){
        return cityService.update(id, city);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCustomer(@PathVariable Integer  id){
        return cityService.delete(id);
    }
}
