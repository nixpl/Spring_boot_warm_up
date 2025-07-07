package com.example.demo.service;

import com.example.demo.dto.AddressCreateDTO;
import com.example.demo.dto.AddressGetDTO;
import com.example.demo.dto.AddressUpdateDTO;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.model.City;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository repository;
    private final CityRepository cityRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository repository, CityRepository cityRepository, AddressMapper addressMapper) {
        this.repository = repository;
        this.cityRepository = cityRepository;
        this.addressMapper = addressMapper;
    }

    public List<AddressGetDTO> getAll() {
        List<Address> addresses = repository.findAll();
        return addresses.stream().map(addressMapper::toGetDTO).toList();
    }

    public AddressGetDTO getById(Integer id) {
        Address a = repository.findById(id).orElseThrow(()->
                new EntityNotFoundException("addressId"));
        return addressMapper.toGetDTO(a);
    }


    public ResponseEntity<AddressGetDTO> create(AddressCreateDTO dto) {
        City city = cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("city_id"));
        if (repository.findByAddressAndAddress2AndDistrictAndCityAndPostalCodeAndPhone(dto.address(), dto.address2(), dto.district(), city, dto.postalCode(), dto.phone()).isPresent())
            throw new DataIntegrityViolationException("Record with such fields already exists");

        Address address = addressMapper.toEntity(dto);
        address.setCity(city);
        Address saved = repository.save(address);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressMapper.toGetDTO(saved));
    }

    public ResponseEntity<Address> delete(Integer id) {
        Optional<Address> address = repository.findById(id);
        if(address.isPresent()){
            repository.delete(address.get());
            return ResponseEntity.ok().build();      }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<AddressGetDTO> update(Integer id, AddressUpdateDTO dto) {
        Optional<Address> opt_address = repository.findById(id);
        if(opt_address.isPresent()){
            Address address = opt_address.get();

            if(!dto.address().isEmpty())
                address.setAddress(dto.address());

            if(!dto.address2().isEmpty())
                address.setAddress2(dto.address2());

            if(!dto.district().isEmpty())
                address.setDistrict(dto.district());

            if(dto.cityId() != null) {
                City city = cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("cityId"));
                address.setCity(city);

            }
            if(!dto.postalCode().isEmpty())
                address.setPostalCode(dto.postalCode());

            if(!dto.phone().isEmpty())
                address.setPhone(dto.phone());

            return ResponseEntity.ok().body(addressMapper.toGetDTO(address));
        }
        else{
            throw new EntityNotFoundException("addressId");
        }
    }


}
