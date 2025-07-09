package com.example.demo.controller;

import com.example.demo.annotations.ForAddress;
import com.example.demo.dto.AddressCreateDTO;
import com.example.demo.dto.AddressGetDTO;
import com.example.demo.dto.AddressUpdateDTO;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/all")
    public Page<AddressGetDTO> getAddresses(@RequestParam(required = false) Map<String, String> params,
                                            @ForAddress @PageableDefault(page = 0, size = 10, sort = "addressId") Pageable pageable){
        return addressService.getAll(params, pageable);
    }

    @GetMapping("/{id}")
    public AddressGetDTO getAddress(@PathVariable Integer id){
        return addressService.getById(id);
    }

    @PostMapping
    public ResponseEntity<AddressGetDTO> createAddress(@Valid @RequestBody AddressCreateDTO address){
        return addressService.create(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressGetDTO> updateAddress(@PathVariable Integer id, @Valid @RequestBody AddressUpdateDTO address){
        return addressService.update(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Integer id){
        return addressService.delete(id);
    }

}
