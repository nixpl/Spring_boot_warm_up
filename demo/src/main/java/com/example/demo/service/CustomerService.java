package com.example.demo.service;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerCreateNoAddressDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .map(c -> new CustomerGetDTO(c.getFirst_name(), c.getLast_name(), c.getEmail(), c.getAddress()))
                .toList();
    }

    public CustomerGetDTO getById(Integer id) {
        Optional<Customer> opt_customer = customerRepository.findById(id);
        if(opt_customer.isPresent()){
            Customer c = opt_customer.get();
            return new CustomerGetDTO(c.getFirst_name(), c.getLast_name(), c.getEmail(), c.getAddress());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong customer_id");
    }

    public ResponseEntity<Customer> create(CustomerCreateDTO dto) {

        if(customerRepository.findByEmail(dto.email()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");

        if(dto.first_name().isEmpty() || dto.last_name().isEmpty()|| dto.email().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and email cannot be empty");

        // TODO szukanie czy istnieje adress_id i store_id(?)

        Customer customer = new Customer();

        customer.setStore_id(dto.store_id());
        customer.setFirst_name(dto.first_name());
        customer.setLast_name(dto.last_name());
        customer.setEmail(dto.email());

        customer.setAddress(dto.address());

        customer.setActive(dto.active());
        customer.setActivebool(true);
        customer.setCreate_date(new Date());
        customer.setLast_update(new Date());

        Customer saved = customerRepository.save(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<Customer> createWithExistingAddress(Integer address_id ,CustomerCreateNoAddressDTO dto) {
        Optional<Address> opt_address = addressRepository.findById(address_id);
        Address address = opt_address.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong address_id"));
        CustomerCreateDTO dto_with_address = new CustomerCreateDTO(dto.store_id(), dto.first_name(), dto.last_name(), dto.email(), address, dto.active());

        return create(dto_with_address);
    }


    public ResponseEntity<Customer> delete(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            customerRepository.delete(customer.get());
            return ResponseEntity.ok().build();      }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong customer_id");
        }
    }

    public ResponseEntity<Customer> update(Integer id, CustomerUpdateDTO dto) {
        Optional<Customer> opt_customer = customerRepository.findById(id);
        if(opt_customer.isPresent()){
            Customer customer = opt_customer.get();
            if(dto.store_id() != null && dto.store_id() >= 0)
                // TODO sprawdzenie czy istnieje
                customer.setStore_id(dto.store_id());

            if(dto.first_name() != null && !dto.first_name().isEmpty())
                customer.setFirst_name(dto.first_name());

            if(dto.last_name() != null && !dto.last_name().isEmpty())
                customer.setLast_name(dto.last_name());

            if(dto.email() != null && !dto.email().isEmpty()) {
                if (customerRepository.findByEmail(dto.email()).isPresent() && !dto.email().equals(customer.getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong email");
                }
                customer.setEmail(dto.email());
            }

            if(dto.address() != null && dto.address().getAddress_id() >= 0)
                // TODO sprawdzenie czy istnieje
                customer.setAddress(dto.address());

            if(dto.active() != null && dto.active() > 0) // ???
                customer.setActive(dto.active());

            if(dto.activebool() != null && dto.activebool() != customer.getActivebool())
                customer.setActivebool(dto.activebool());

            customer.setLast_update(new Date());
            
            customerRepository.save(customer);
            return ResponseEntity.ok().body(customer);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong customer_id");
        }
    }
}
