package com.example.demo.service;

import com.example.demo.exception.TooManyFiltersException;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.specification.CustomerSpecifications;
import com.example.demo.dto.CustomerCreateDTO;
import com.example.demo.dto.CustomerGetDTO;
import com.example.demo.dto.CustomerUpdateDTO;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
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

    public Page<CustomerGetDTO> getAll(Map<String, String> params, Pageable pageable) {
        log.info("Attempting to retrieve all customers with parameters: {} and pageable: {}", params, pageable);
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<Customer> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        filterParams.remove("page");
        filterParams.remove("size");
        filterParams.remove("sort");
        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            log.info("Applying search term specification: {}", searchTerm);
            spec = spec.and(CustomerSpecifications.hasSearchTerm(searchTerm));
        }

        if (filterParams.size() > 1) {
            throw new TooManyFiltersException(filterParams.keySet());
        }

        if (!filterParams.isEmpty()) {
            Map.Entry<String, String> entry = filterParams.entrySet().iterator().next();
            log.info("Applying filter specification with key: '{}' and value: '{}'", entry.getKey(), entry.getValue());
            spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
        }

        Page<CustomerGetDTO> result = customerRepository.findAll(spec, pageable).map(mapper::toGetDTO);
        log.info("Successfully retrieved {} customers on page {}", result.getNumberOfElements(), pageable.getPageNumber());
        return result;
    }

    private Specification<Customer> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "firstName" -> (root, query, cb) -> cb.equal(root.get("firstName"), value);
            case "lastName" -> (root, query, cb) -> cb.equal(root.get("lastName"), value);
            case "email" -> (root, query, cb) -> cb.equal(root.get("email"), value);
            case "active" -> (root, query, cb) -> cb.equal(root.get("active"), Integer.parseInt(value));
            case "address" -> (root, query, cb) -> cb.equal(root.get("address").get("address"), value);
            case "address2" -> (root, query, cb) -> cb.equal(root.get("address").get("address2"), value);
            case "district" -> (root, query, cb) -> cb.equal(root.get("address").get("district"), value);
            case "city" -> (root, query, cb) -> cb.equal(root.get("address").get("city").get("city"), value);
            case "country" -> (root, query, cb) -> cb.equal(root.get("address").get("city").get("country").get("country"), value);
            default -> throw new UnknownFilterParameterException(key);
        };
    }

    public CustomerGetDTO getById(Integer id) {
        log.info("Attempting to retrieve customer with ID: {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("customerId"));
        log.info("Successfully retrieved customer with ID: {}", id);
        return mapper.toGetDTO(customer);
    }

    public ResponseEntity<CustomerGetDTO> create(CustomerCreateDTO dto) {
        log.info("Attempting to create a new customer with DTO: {}", dto);

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
        log.info("Successfully created and saved customer with ID: {}", saved.getCustomerId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toGetDTO(saved));
    }


    public ResponseEntity<Customer> delete(Integer id) {
        log.info("Attempting to delete customer with ID: {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            customerRepository.delete(customer.get());
            log.info("Successfully deleted customer with ID: {}", id);
            return ResponseEntity.ok().build();      }
        else{
            throw new EntityNotFoundException("customerId");
        }
    }

    public ResponseEntity<CustomerGetDTO> update(Integer id, CustomerUpdateDTO dto) {
        log.info("Attempting to update customer with ID: {} using DTO: {}", id, dto);
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
            log.info("Successfully updated customer with ID: {}", saved.getCustomerId());
            return ResponseEntity.ok().body(mapper.toGetDTO(saved));
        }
        else{
            throw new EntityNotFoundException("customerId");
        }
    }
}