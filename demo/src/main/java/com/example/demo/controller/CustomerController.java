package com.example.demo.controller;

import com.example.demo.annotations.ForCustomer;
import com.example.demo.dto.customer.CustomerCreateDTO;
import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.dto.customer.CustomerUpdateDTO;
import com.example.demo.mapper.CustomerMapper;
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

    private final CustomerMapper customerMapper;

    private final CustomerService customerService;

    public CustomerController(CustomerMapper customerMapper, CustomerService customerService) {
        this.customerMapper = customerMapper;
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public Page<CustomerGetDTO> getCustomers(
            @RequestParam(required = false)Map<String, String> params,
            @ForCustomer @PageableDefault(page = 0, size = 10, sort = "customerId") Pageable pageable){
        log.info("Received request to get all customers with params: {} and pageable: {}", params, pageable);
        return customerService.getAll(params, pageable);
    }

    @GetMapping("/{id}")
    public CustomerGetDTO getCustomer(@PathVariable Integer id){
        log.info("Received request to get customer with ID: {}", id);
        return customerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerGetDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customer){
        log.info("Received request to create a new customer: {}", customer.toString());
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerGetDTO> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerUpdateDTO customer){
        log.info("Received request to update customer with ID: {} with data: {}", id, customer.toString());
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id){
        log.info("Received request to delete customer with ID: {}", id);
        return customerService.delete(id);
    }
}