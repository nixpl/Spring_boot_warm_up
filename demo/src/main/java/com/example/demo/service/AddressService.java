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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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

public Page<AddressGetDTO> getAll(Map<String, String> filter, Pageable pageable) {
    if  (filter.isEmpty())
        return addressRepository.findAll(pageable).map(addressMapper::toGetDTO);

    if (filter.size() > 1)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are allowed to filter only by one parameter");

    String key = filter.keySet().iterator().next();
    String value = filter.values().iterator().next();

    var method = getStringPageFunction(pageable, key);

    return method.apply(value).map(addressMapper::toGetDTO);
}

    private Function<String, Page<Address>> getStringPageFunction(Pageable pageable, String key) {
        Map<String, Function<String, Page<Address>>> filterMethods = Map.of(
                "address", v -> addressRepository.findByAddress(v, pageable),
                "address2", v -> addressRepository.findByAddress2(v, pageable),
                "district", v -> addressRepository.findByDistrict(v, pageable),
                "city", v -> addressRepository.findByCity_City(v, pageable),
                "country", v -> addressRepository.findByCity_Country_Country(v, pageable)
        );

        var method = filterMethods.get(key);

        if (method == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown filter parameter: " + key);
        }
        return method;
    }

    public AddressGetDTO getById(Integer id) {
        Address a = addressRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("addressId"));
        return addressMapper.toGetDTO(a);
    }


    public ResponseEntity<AddressGetDTO> create(AddressCreateDTO dto) {
        City city = cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("cityId"));
        if (addressRepository.findByAddressAndAddress2AndDistrictAndCityAndPostalCodeAndPhone(dto.address(), dto.address2(), dto.district(), city, dto.postalCode(), dto.phone()).isPresent())
            throw new DataIntegrityViolationException("Record with such fields already exists");

        Address address = addressMapper.toEntity(dto);
        address.setCity(city);
        Address saved = addressRepository.save(address);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressMapper.toGetDTO(saved));
    }

    public ResponseEntity<Address> delete(Integer id) {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isPresent()){
            addressRepository.delete(address.get());
            return ResponseEntity.ok().build();      }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<AddressGetDTO> update(Integer id, AddressUpdateDTO dto) {
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
            return ResponseEntity.ok().body(addressMapper.toGetDTO(address));
        }
        else{
            throw new EntityNotFoundException("addressId");
        }
    }


}
