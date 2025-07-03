package com.example.demo.controller;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/all")
    public List<Address> getAddresses(){
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable Long id){
        return addressService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(AddressDTO address){
        return addressService.create(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, AddressDTO address){
        return addressService.update(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Long id){
        return addressService.delete(id);
    }

}
