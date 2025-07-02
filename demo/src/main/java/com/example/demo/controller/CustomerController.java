package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/customer")
public class CustomerController {

    private CustomerService customerService;

    @GetMapping("/all")
    public List<Customer> getCustomers(){
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
        return customerService.getById(id);
    }

    @PostMapping
    public void createCustomer(Customer customer){
        // TODO
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable Long id, Customer customer){
        // TODO
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id){
        // TODO
    }
}
