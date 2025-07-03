package com.example.demo.service;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public ResponseEntity<Customer> getById(Long id) {
        return repository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Customer> create(CustomerCreateDTO dto) {

        if(repository.findByEmail(dto.email()).isPresent())
            return ResponseEntity.badRequest().build();

        if(dto.first_name().isEmpty() || dto.last_name().isEmpty()|| dto.email().isEmpty())
            return ResponseEntity.badRequest().build();

        // TODO szukanie czy istnieje adress_id i store_id(?)

        Customer customer = new Customer();

        customer.setStore_id(dto.store_id());
        customer.setFirst_name(dto.first_name());
        customer.setLast_name(dto.last_name());
        customer.setEmail(dto.email());
        customer.setAddress_id(dto.address_id());
        customer.setActive(dto.active());
        customer.setActivebool(true);
        customer.setCreate_date(new Date());
        customer.setLast_update(new Date());

        Customer saved = repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }


    public ResponseEntity<Customer> delete(Long id) {
        Optional<Customer> customer = repository.findById(id);
        if(customer.isPresent()){
            repository.delete(customer.get());
            return ResponseEntity.ok().build();      }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
