package com.example.demo.controller;

import com.example.demo.annotations.ForAddress;
import com.example.demo.dto.address.AddressCreateDTO;
import com.example.demo.dto.address.AddressGetDTO;
import com.example.demo.dto.address.AddressUpdateDTO;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/all")
    public Page<AddressGetDTO> getAddresses(@RequestParam(required = false) Map<String, String> params,
                                            @ForAddress @PageableDefault(page = 0, size = 10, sort = "addressId") Pageable pageable){
        log.info("Received request to get all addresses with params: {} and pageable: {}", params, pageable);
        return addressService.getAll(params, pageable);
    }

    @GetMapping("/{id}")
    public AddressGetDTO getAddress(@PathVariable Integer id){
        log.info("Received request to get address with ID: {}", id);
        return addressService.getById(id);
    }

    @PostMapping
    public ResponseEntity<AddressGetDTO> createAddress(@Valid @RequestBody AddressCreateDTO address){
        log.info("Received request to create a new address: {}", address);
        return addressService.create(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressGetDTO> updateAddress(@PathVariable Integer id, @Valid @RequestBody AddressUpdateDTO address){
        log.info("Received request to update address with ID: {} with data: {}", id, address);
        return addressService.update(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Integer id){
        log.info("Received request to delete address with ID: {}", id);
        return addressService.delete(id);
    }
}