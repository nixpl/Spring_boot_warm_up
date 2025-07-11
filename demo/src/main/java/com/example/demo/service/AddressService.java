package com.example.demo.service;

import com.example.demo.exception.TooManyFiltersException;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.specification.AddressSpecifications;
import com.example.demo.dto.AddressCreateDTO;
import com.example.demo.dto.AddressGetDTO;
import com.example.demo.dto.AddressUpdateDTO;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.model.City;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, CityRepository cityRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.addressMapper = addressMapper;
    }

    public Page<AddressGetDTO> getAll(Map<String, String> params, Pageable pageable) {
        log.info("Attempting to retrieve all addresses with parameters: {} and pageable: {}", params, pageable);
        Map<String, String> filterParams = new java.util.HashMap<>(params);

        Specification<Address> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        filterParams.remove("page");
        filterParams.remove("size");
        filterParams.remove("sort");
        String searchTerm = filterParams.remove("search");
        if (searchTerm != null && !searchTerm.isBlank()) {
            log.info("Applying search term specification: {}", searchTerm);
            spec = spec.and(AddressSpecifications.hasSearchTerm(searchTerm));
        }

        if (!filterParams.isEmpty()) {
            for (Map.Entry<String, String> entry : filterParams.entrySet()){
                log.info("Applying filter specification with key: '{}' and value: '{}'", entry.getKey(), entry.getValue());
                spec = spec.and(createFilterSpecification(entry.getKey(), entry.getValue()));
            }
        }

        Page<AddressGetDTO> result = addressRepository.findAll(spec, pageable).map(addressMapper::toGetDTO);
        log.info("Successfully retrieved {} addresses on page {}", result.getNumberOfElements(), pageable.getPageNumber());
        return result;
    }

    private Specification<Address> createFilterSpecification(String key, String value) {
        return switch (key) {
            case "address" -> (root, query, cb) -> cb.equal(root.get("address"), value);
            case "address2" -> (root, query, cb) -> cb.equal(root.get("address2"), value);
            case "district" -> (root, query, cb) -> cb.equal(root.get("district"), value);
            case "city" -> (root, query, cb) -> cb.equal(root.get("city").get("city"), value);
            case "country" -> (root, query, cb) -> cb.equal(root.get("city").get("country").get("country"), value);
            default -> throw new UnknownFilterParameterException(key);
        };
    }

    public AddressGetDTO getById(Integer id) {
        log.info("Attempting to retrieve address with ID: {}", id);
        Address a = addressRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("addressId"));
        log.info("Successfully retrieved address with ID: {}", id);
        return addressMapper.toGetDTO(a);
    }


    public ResponseEntity<AddressGetDTO> create(AddressCreateDTO dto) {
        log.info("Attempting to create a new address with DTO: {}", dto);
        City city = cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("cityId"));
        if (addressRepository.findByAddressAndAddress2AndDistrictAndCityAndPostalCodeAndPhone(dto.address(), dto.address2(), dto.district(), city, dto.postalCode(), dto.phone()).isPresent())
            throw new DataIntegrityViolationException("Record with such fields already exists");

        Address address = addressMapper.toEntity(dto);
        address.setCity(city);
        Address saved = addressRepository.save(address);
        log.info("Successfully created and saved address with ID: {}", saved.getAddressId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressMapper.toGetDTO(saved));
    }

    public ResponseEntity<Address> delete(Integer id) {
        log.info("Attempting to delete address with ID: {}", id);
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()){
            addressRepository.delete(address.get());
            log.info("Successfully deleted address with ID: {}", id);
            return ResponseEntity.ok().build();      }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<AddressGetDTO> update(Integer id, AddressUpdateDTO dto) {
        log.info("Attempting to update address with ID: {} using DTO: {}", id, dto);
        Optional<Address> optAddress = addressRepository.findById(id);
        if(optAddress.isPresent()){
            Address address = optAddress.get();

            if(dto.address() != null && !dto.address().isEmpty())
                address.setAddress(dto.address());

            if(dto.address2() != null && !dto.address2().isEmpty())
                address.setAddress2(dto.address2());

            if(dto.district() != null && !dto.district().isEmpty())
                address.setDistrict(dto.district());

            if(dto.cityId() != null) {
                City city = cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("cityId"));
                address.setCity(city);

            }
            if(dto.postalCode() != null && !dto.postalCode().isEmpty())
                address.setPostalCode(dto.postalCode());

            if(dto.phone() != null && !dto.phone().isEmpty())
                address.setPhone(dto.phone());

            Address saved = addressRepository.save(address);
            log.info("Successfully updated address with ID: {}", saved.getAddressId());
            return ResponseEntity.ok().body(addressMapper.toGetDTO(address));
        }
        else{
            throw new EntityNotFoundException("addressId");
        }
    }


}