package com.example.demo.controller;

import com.example.demo.dto.CityDTO;
import com.example.demo.model.City;
import com.example.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping("/all")
    public List<City> getCites(){
        return cityService.getAll();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Integer  id){
        return cityService.getById(id);
    }

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody CityDTO city){
        return cityService.create(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Integer  id, @RequestBody CityDTO city){
        return cityService.update(id, city);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCustomer(@PathVariable Integer  id){
        return cityService.delete(id);
    }
}
