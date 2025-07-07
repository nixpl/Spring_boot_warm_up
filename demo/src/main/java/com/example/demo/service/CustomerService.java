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
                                c.getFirstName(),
                                c.getLastName(),
                                c.getEmail(),
                                new AddressDTO(c.getAddress().getAddress(),
                                        c.getAddress().getAddress2(),
                                        c.getAddress().getDistrict(),
                                        c.getAddress().getCity().getCityId(),
                                        c.getAddress().getPostalCode(),
                                        c.getAddress().getPhone()
                                        )))
                .toList();
    }

    public CustomerGetDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("customer_id"));
        return new CustomerGetDTO(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                new AddressDTO(customer.getAddress().getAddress(),
                        customer.getAddress().getAddress2(),
                        customer.getAddress().getDistrict(),
                        customer.getAddress().getCity().getCityId(),
                        customer.getAddress().getPostalCode(),
                        customer.getAddress().getPhone()
                ));
    }

    public ResponseEntity<Customer> create(CustomerCreateDTO dto) {

        if(customerRepository.findByEmail(dto.email()).isPresent())
            throw new DataIntegrityViolationException("Email is already taken");

        if(dto.firstName().isEmpty() || dto.lastName().isEmpty()|| dto.email().isEmpty())
            throw new DataIntegrityViolationException("Name and email cannot be empty");
        Customer customer = new Customer();

        customer.setStoreId(dto.storeId());
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());

        Address address = addressRepository.findById(dto.addressId()).orElseThrow(() ->new EntityNotFoundException("address_id"));
        customer.setAddress(address);

        customer.setActive(dto.active());
        customer.setActivebool(true);
        customer.setCreateDate(new Date());
        customer.setLastUpdate(new Date());

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
            if(dto.storeId() != null && dto.storeId() >= 0)
                customer.setStoreId(dto.storeId());

            if(dto.firstName() != null && !dto.firstName().isEmpty())
                customer.setFirstName(dto.firstName());

            if(dto.lastName() != null && !dto.lastName().isEmpty())
                customer.setLastName(dto.lastName());

            if(dto.email() != null && !dto.email().isEmpty()) {
                if (customerRepository.findByEmail(dto.email()).isPresent() && !dto.email().equals(customer.getEmail())) {
                    throw new DataIntegrityViolationException("Wrong email");
                }
                customer.setEmail(dto.email());
            }

            if(dto.addressId() != null) {
                Address address = addressRepository.findById(dto.addressId()).orElseThrow(() -> new EntityNotFoundException("address_id"));
                customer.setAddress(address);
            }

            if(dto.active() != null)
                customer.setActive(dto.active());

            if(dto.activebool() != null && dto.activebool() != customer.getActivebool())
                customer.setActivebool(dto.activebool());

            customer.setLastUpdate(new Date());

            customerRepository.save(customer);
            return ResponseEntity.ok().build();
        }
        else{
            throw new EntityNotFoundException("customer_id");
        }
    }
}
