package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    CustomerRepository repository;

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public ResponseEntity<Customer> getById(Long id) {
        return repository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
}
