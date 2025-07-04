package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    public List<CustomerGetDTO> getCustomers(){
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public CustomerGetDTO getCustomer(@PathVariable Integer id){
        return customerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid CustomerCreateDTO customer){
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, CustomerUpdateDTO customer){
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id){
        return customerService.delete(id);
    }
}
