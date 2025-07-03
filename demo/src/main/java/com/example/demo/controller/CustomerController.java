package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
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
    public List<Customer> getCustomers(){
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
        return customerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(CustomerCreateDTO customer){
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable Long id, Customer customer){
        // TODO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id){
        return customerService.delete(id);
    }
}
