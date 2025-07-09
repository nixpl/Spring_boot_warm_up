package com.example.demo.controller;

import com.example.demo.annotations.ForCustomer;
import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public Page<CustomerGetDTO> getCustomers(
            @RequestParam(required = false)Map<String, String> filter,
            @ForCustomer @PageableDefault(page = 0, size = 10, sort = "customerId") Pageable pageable){
        log.info("Received request to get all customers with filter: {} and pageable: {}", filter, pageable);
        return customerService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public CustomerGetDTO getCustomer(@PathVariable Integer id){
        log.info("Received request to get customer with ID: {}", id);
        return customerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerGetDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customer){
        log.info("Received request to create a new customer: {}", customer);
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerGetDTO> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerUpdateDTO customer){
        log.info("Received request to update customer with ID: {} with data: {}", id, customer);
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id){
        log.info("Received request to delete customer with ID: {}", id);
        return customerService.delete(id);
    }
}