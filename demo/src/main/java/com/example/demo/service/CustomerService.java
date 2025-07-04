package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;


    public List<CustomerGetDTO> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(c ->
                        new CustomerGetDTO(
                                c.getFirst_name(),
                                c.getLast_name(),
                                c.getEmail(),
                                new AddressDTO(c.getAddress().getAddress(),
                                        c.getAddress().getAddress2(),
                                        c.getAddress().getDistrict(),
                                        c.getAddress().getCity().getCity_id(),
                                        c.getAddress().getPostal_code(),
                                        c.getAddress().getPhone()
                                        )))
                .toList();
    }

    public CustomerGetDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("customer_id"));
        return new CustomerGetDTO(
                customer.getFirst_name(),
                customer.getLast_name(),
                customer.getEmail(),
                new AddressDTO(customer.getAddress().getAddress(),
                        customer.getAddress().getAddress2(),
                        customer.getAddress().getDistrict(),
                        customer.getAddress().getCity().getCity_id(),
                        customer.getAddress().getPostal_code(),
                        customer.getAddress().getPhone()
                ));
    }

    public ResponseEntity<Customer> create(CustomerCreateDTO dto) {

        if(customerRepository.findByEmail(dto.email()).isPresent())
            throw new DataIntegrityViolationException("Email is already taken");

        if(dto.first_name().isEmpty() || dto.last_name().isEmpty()|| dto.email().isEmpty())
            throw new DataIntegrityViolationException("Name and email cannot be empty");
        Customer customer = new Customer();

        customer.setStore_id(dto.store_id());
        customer.setFirst_name(dto.first_name());
        customer.setLast_name(dto.last_name());
        customer.setEmail(dto.email());

        Address address = addressRepository.findById(dto.address_id()).orElseThrow(() ->new EntityNotFoundException("address_id"));
        customer.setAddress(address);

        customer.setActive(dto.active());
        customer.setActivebool(true);
        customer.setCreate_date(new Date());
        customer.setLast_update(new Date());

        customerRepository.save(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    public ResponseEntity<Customer> delete(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            customerRepository.delete(customer.get());
            return ResponseEntity.ok().build();      }
        else{
            throw new EntityNotFoundException("customer_id");
        }
    }

    public ResponseEntity<Customer> update(Integer id, CustomerUpdateDTO dto) {
        Optional<Customer> opt_customer = customerRepository.findById(id);
        if(opt_customer.isPresent()){
            Customer customer = opt_customer.get();
            if(dto.store_id() != null && dto.store_id() >= 0)
                customer.setStore_id(dto.store_id());

            if(dto.first_name() != null && !dto.first_name().isEmpty())
                customer.setFirst_name(dto.first_name());

            if(dto.last_name() != null && !dto.last_name().isEmpty())
                customer.setLast_name(dto.last_name());

            if(dto.email() != null && !dto.email().isEmpty()) {
                if (customerRepository.findByEmail(dto.email()).isPresent() && !dto.email().equals(customer.getEmail())) {
                    throw new DataIntegrityViolationException("Wrong email");
                }
                customer.setEmail(dto.email());
            }

            if(dto.address_id() != null) {
                Address address = addressRepository.findById(dto.address_id()).orElseThrow(() -> new EntityNotFoundException("address_id"));
                customer.setAddress(address);
            }

            if(dto.active() != null)
                customer.setActive(dto.active());

            if(dto.activebool() != null && dto.activebool() != customer.getActivebool())
                customer.setActivebool(dto.activebool());

            customer.setLast_update(new Date());

            customerRepository.save(customer);
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("customer_id");
        }
    }
}
