package com.example.demo.service;

import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }

    public Page<CustomerGetDTO> getAll(Map<String, String> filter, Pageable pageable) {
        if  (filter.isEmpty())
            return customerRepository.findAll(pageable).map(mapper::toGetDTO);

        if (filter.size() > 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are allowed to filter only by one parameter");

        String key = filter.keySet().iterator().next();
        String value = filter.values().iterator().next();

        var method = getStringPageFunction(pageable, key);

        return method.apply(value).map(mapper::toGetDTO);
    }

    private Function<String, Page<Customer>> getStringPageFunction(Pageable pageable, String key) {
        Map<String, Function<String, Page<Customer>>> filterMethods = Map.of(
                "firstName", v -> customerRepository.findByFirstName(v, pageable),
                "lastName", v -> customerRepository.findByLastName(v, pageable),
                "email", v -> customerRepository.findByEmail(v, pageable),
                "active", v -> customerRepository.findByActive(Integer.parseInt(v), pageable),
                "address", v -> customerRepository.findByAddress_Address(v, pageable),
                "city", v -> customerRepository.findByAddress_City_City(v, pageable),
                "country", v -> customerRepository.findByAddress_City_Country(v, pageable)
        );

        var method = filterMethods.get(key);

        if (method == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown filter parameter: " + key);
        }
        return method;
    }

    public CustomerGetDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("customerId"));
        return mapper.toGetDTO(customer);
    }

    public ResponseEntity<CustomerGetDTO> create(CustomerCreateDTO dto) {

        if (customerRepository.findByEmail(dto.email()).isPresent()) {
            throw new DataIntegrityViolationException("Email is already taken");
        }

        if (dto.firstName().isEmpty() || dto.lastName().isEmpty() || dto.email().isEmpty()) {
            throw new DataIntegrityViolationException("Name and email cannot be empty");
        }

        Customer customer = mapper.toEntity(dto);

        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new EntityNotFoundException("addressId"));

        customer.setAddress(address);

        Customer saved = customerRepository.save(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toGetDTO(saved));
    }


    public ResponseEntity<Customer> delete(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            customerRepository.delete(customer.get());
            return ResponseEntity.ok().build();      }
        else{
            throw new EntityNotFoundException("customerId");
        }
    }

    public ResponseEntity<CustomerGetDTO> update(Integer id, CustomerUpdateDTO dto) {
        Optional<Customer> optCustomer = customerRepository.findById(id);
        if(optCustomer.isPresent()){
            Customer customer = optCustomer.get();
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
                Address address = addressRepository.findById(dto.addressId()).orElseThrow(() -> new EntityNotFoundException("addressId"));
                customer.setAddress(address);
            }

            if(dto.active() != null)
            {
                if (dto.active() == 0 || dto.active() == 1)
                    customer.setActive(dto.active());
                else
                    throw new DataIntegrityViolationException("active must be 0 or 1");
            }

            if(dto.activebool() != null)
                customer.setActivebool(dto.activebool());

            customer.setLastUpdate(new Date());

            Customer saved = customerRepository.save(customer);
            return ResponseEntity.ok().body(mapper.toGetDTO(saved));
        }
        else{
            throw new EntityNotFoundException("customerId");
        }
    }
}
