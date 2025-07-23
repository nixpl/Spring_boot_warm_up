package com.example.demo.service;

import com.example.demo.api.DisifyApi;
import com.example.demo.api.GenderizeApi;
import com.example.demo.exception.*;
import com.example.demo.exception.info.ExceptionInfo;
import com.example.demo.specification.CustomerSpecifications;
import com.example.demo.dto.customer.CustomerCreateDTO;
import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.dto.customer.CustomerUpdateDTO;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
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

        if (!filterParams.isEmpty()) {
            for (Map.Entry<String, String> entry : filterParams.entrySet()){
                log.info("Applying filter specification with key: '{}' and value: '{}'", entry.getKey(), entry.getValue());
                spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
            }
        }

        Page<CustomerGetDTO> result = customerRepository.findAll(spec, pageable).map(mapper::toGetDTO);
        log.info("Successfully retrieved {} customers on page {}", result.getNumberOfElements(), pageable.getPageNumber());
        return result;
    }

    private Specification<Customer> createFilterSpecification(String key, String value) {
        Map<String,Integer> statusToActiveValues = Map.of(  "active", 1,
                                                            "inactive", 0);

        return switch (key) {
            case "firstName" -> (root, query, cb) -> cb.equal(root.get("firstName"), value);
            case "lastName" -> (root, query, cb) -> cb.equal(root.get("lastName"), value);
            case "email" -> (root, query, cb) -> cb.equal(root.get("email"), value);
            case "status" -> (root, query, cb) -> {
                if (statusToActiveValues.containsKey(value))
                    return cb.equal(root.get("active"), statusToActiveValues.get(value));
                else
                    throw new WrongFilterArgumentException(ExceptionInfo.WRONG_CUSTOMER_FILTER_ARGUMENT, value, key);
            };
            case "address" -> (root, query, cb) -> cb.equal(root.get("address").get("address"), value);
            case "address2" -> (root, query, cb) -> cb.equal(root.get("address").get("address2"), value);
            case "district" -> (root, query, cb) -> cb.equal(root.get("address").get("district"), value);
            case "city" -> (root, query, cb) -> cb.equal(root.get("address").get("city").get("city"), value);
            case "country" -> (root, query, cb) -> cb.equal(root.get("address").get("city").get("country").get("country"), value);
            default -> throw new UnknownFilterParameterException(ExceptionInfo.UNKNOWN_CUSTOMER_FILTER_PARAMETER, key);
        };
    }

    public CustomerGetDTO getById(Integer id) {
        log.info("Attempting to retrieve customer with ID: {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_CUSTOMER_NOT_FOUND, id));
        log.info("Successfully retrieved customer with ID: {}", id);
        return mapper.toGetDTO(customer);
    }

    public ResponseEntity<CustomerGetDTO> create(CustomerCreateDTO dto) {
        log.info("Attempting to create a new customer with DTO: {}", dto);

        if (customerRepository.findByEmail(dto.email()).isPresent()) {
            throw new DataIntegrityViolationException(ExceptionInfo.CUSTOMER_EMAIL_TAKEN, dto.email());
        }

        if (DisifyApi.isDisposable(dto.email())) {throw new DisposableEmailException(ExceptionInfo.CUSTOMER_EMAIL_IS_DISPOSABLE, dto.email());}

        Customer customer = mapper.toEntity(dto);
        customer.setGender(GenderizeApi.deduceGender(dto.firstName()));

        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_ADDRESS_NOT_FOUND, dto.addressId()));

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
            throw new EntityNotFoundException(ExceptionInfo.ENTITY_CUSTOMER_NOT_FOUND, id);
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

            if(dto.gender() != null)
                customer.setGender(dto.gender());

            if(dto.email() != null && !dto.email().isEmpty()) {
                if (customerRepository.findByEmail(dto.email()).isPresent() && !dto.email().equals(customer.getEmail())) {
                    throw new DataIntegrityViolationException(ExceptionInfo.CUSTOMER_EMAIL_TAKEN, dto.email());
                }
                customer.setEmail(dto.email());
            }

            if(dto.addressId() != null) {
                Address address = addressRepository.findById(dto.addressId()).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_ADDRESS_NOT_FOUND, dto.addressId()));
                customer.setAddress(address);
            }

            if(dto.active() != null)
            {
                if (dto.active() == 0 || dto.active() == 1)
                    customer.setActive(dto.active());
                else
                    throw new DataIntegrityViolationException(ExceptionInfo.INPUT_VALIDATION_ERROR, "active", "active must be one of {0, 1}");
            }

            if(dto.activebool() != null)
                customer.setActivebool(dto.activebool());

            customer.setLastUpdate(new Date());

            Customer saved = customerRepository.save(customer);
            log.info("Successfully updated customer with ID: {}", saved.getCustomerId());
            return ResponseEntity.ok().body(mapper.toGetDTO(saved));
        }
        else{
            throw new EntityNotFoundException(ExceptionInfo.ENTITY_CUSTOMER_NOT_FOUND, id);
        }
    }
}