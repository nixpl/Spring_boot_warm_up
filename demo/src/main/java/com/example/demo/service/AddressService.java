package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
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

    public List<Address> getAll() { return repository.findAll(); }

    public ResponseEntity<Address> getById(Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    public ResponseEntity<Address> create(AddressDTO dto) {

        if(repository.findByPhone(dto.phone()).isPresent())
            return ResponseEntity.badRequest().build();

        Address address = new Address();

        address.setAddress(dto.address());
        address.setAddress2(dto.address2());
        address.setDistrict(dto.district());
        address.setCityId(dto.cityId());
        address.setPostalCode(dto.postalCode());
        address.setPhone(dto.phone());

        Address saved = repository.save(address);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<Address> delete(Long id) {
        Optional<Address> address = repository.findById(id);
        if(address.isPresent()){
            repository.delete(address.get());
            return ResponseEntity.ok().build();      }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Address> update(Long id, AddressDTO dto) {
        Optional<Address> opt_address = repository.findById(id);
        if(opt_address.isPresent()){
            Address address = opt_address.get();

            if(!dto.address().isEmpty())
                address.setAddress(dto.address());

            if(!dto.address2().isEmpty())
                address.setAddress2(dto.address2());

            if(!dto.district().isEmpty())
                address.setDistrict(dto.district());

            if(dto.cityId() != null)
                address.setCityId(dto.cityId());

            if(!dto.postalCode().isEmpty())
                address.setPostalCode(dto.postalCode());

            if(!dto.phone().isEmpty())
                address.setPhone(dto.phone());

            return ResponseEntity.ok().body(address);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong address_id");
        }
    }


}
