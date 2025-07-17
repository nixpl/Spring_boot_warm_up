package com.example.demo.controller;

import com.example.demo.dto.rental.RentalCreateDTO;
import com.example.demo.dto.rental.RentalGetDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Rental;
import com.example.demo.service.RentalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rental")
@Slf4j
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/{id}")
    public RentalGetDTO getRental(@PathVariable Integer id){
        log.info("Received request to get rental with ID: {}", id);

        return rentalService.getById(id);
    }

    @PostMapping
    public ResponseEntity<RentalGetDTO> rentFilm(@Valid @RequestBody RentalCreateDTO rental){
        log.info("Received request to create a new rental: {}", rental.toString());
        return rentalService.rentFilm(rental);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<RentalGetDTO> returnFilm(@PathVariable Integer id){
        log.info("Received request to set rental: {} returnDate", id);
        return rentalService.returnFilm(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rental> deleteRental(@PathVariable Integer id){
        log.info("Received request to delete rental with ID: {}", id);
        return rentalService.delete(id);
    }


}
