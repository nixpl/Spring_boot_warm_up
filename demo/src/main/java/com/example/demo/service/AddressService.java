package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.model.City;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;
    @Autowired
    private CityRepository cityRepository;

    public List<AddressDTO> getAll() {
        List<Address> addresses = repository.findAll();
        return addresses.stream().map(a -> new AddressDTO(a.getAddress(),
                a.getAddress2(), a.getDistrict(),
                a.getCity().getCity_id(),
                a.getPostal_code(), a.getPhone())).toList();
    }

    public AddressDTO getById(Integer id) {
        Address a = repository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong address_id"));

        return new AddressDTO(a.getAddress(),
                a.getAddress2(), a.getDistrict(),
                a.getCity().getCity_id(),
                a.getPostal_code(), a.getPhone());
    }


    public ResponseEntity<Address> create(AddressDTO dto) {

        if(repository.findByPhone(dto.phone()).isPresent())
            return ResponseEntity.badRequest().build();

        Address address = new Address();

        address.setAddress(dto.address());
        address.setAddress2(dto.address2());
        address.setDistrict(dto.district());
        Optional<City> city = cityRepository.findById(dto.city_id());
        if(city.isPresent()){
            address.setCity(city.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
        }
        address.setPostal_code(dto.postal_code());
        address.setPhone(dto.phone());

        repository.save(address);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
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

    public ResponseEntity<Address> update(Integer id, AddressDTO dto) {
        Optional<Address> opt_address = repository.findById(id);
        if(opt_address.isPresent()){
            Address address = opt_address.get();

            if(!dto.address().isEmpty())
                address.setAddress(dto.address());

            if(!dto.address2().isEmpty())
                address.setAddress2(dto.address2());

            if(!dto.district().isEmpty())
                address.setDistrict(dto.district());

            if(dto.city_id() != null) {
                Optional<City> city = cityRepository.findById(dto.city_id());
                if (city.isPresent()) {
                    address.setCity(city.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong city_id");
                }
            }
            if(!dto.postal_code().isEmpty())
                address.setPostal_code(dto.postal_code());

            if(!dto.phone().isEmpty())
                address.setPhone(dto.phone());

            return ResponseEntity.ok().build();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong address_id");
        }
    }


}
