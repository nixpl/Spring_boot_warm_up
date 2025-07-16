package com.example.demo.service;

import com.example.demo.dto.rental.RentalCreateDTO;
import com.example.demo.dto.rental.RentalGetDTO;
import com.example.demo.exception.DataIntegrityViolationException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.info.ExceptionInfo;
import com.example.demo.mapper.RentalMapper;
import com.example.demo.model.Customer;
import com.example.demo.model.Rental;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final RentalMapper rentalMapper;

    public RentalService(RentalRepository rentalRepository, InventoryRepository inventoryRepository, CustomerRepository customerRepository, RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.rentalMapper = rentalMapper;
    }

    public RentalGetDTO getById(Integer id) {
        log.info("Attempting to retrieve rental with ID: {}", id);
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_RENTAL_NOT_FOUND, id));
        log.info("Successfully retrieved rental with ID: {}", id);
        return rentalMapper.toGetDTO(rental);
    }

    public ResponseEntity<RentalGetDTO> rentFilm(RentalCreateDTO dto) {
        log.info("Attempting to create a new rental with DTO: {}", dto);
        Rental rental = rentalMapper.toEntity(dto);
        for(Rental r : rentalRepository.findByInventory(rental.getInventory())){
            if(r.getReturnDate() == null) { throw new DataIntegrityViolationException(ExceptionInfo.FILM_IS_NOT_AVAILABLE, dto.customerId());}
        }

        for(Rental r : rentalRepository.findByCustomer(rental.getCustomer())){
            if(r.getReturnDate() == null) { throw new DataIntegrityViolationException(ExceptionInfo.FILM_IS_NOT_AVAILABLE, dto.customerId());}
        }

        rental.setInventory(inventoryRepository.findById(dto.inventoryId()).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_INVENTORY_NOT_FOUND, dto.inventoryId())));
        rental.setCustomer(customerRepository.findById(dto.customerId()).orElseThrow(() -> new EntityNotFoundException(ExceptionInfo.ENTITY_CUSTOMER_NOT_FOUND, dto.customerId())));
        rental.setStaffId(dto.staffId());

        Rental saved = rentalRepository.save(rental);
        log.info("Successfully created and saved rental with ID: {}", saved.getRentalId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rentalMapper.toGetDTO(saved));

    }

    public ResponseEntity<RentalGetDTO> returnFilm(Integer id){
        log.info("Attempting to set current date as rental returnDate parameter: {}", id);
        Rental rental = rentalRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(ExceptionInfo.ENTITY_RENTAL_NOT_FOUND, id));
        Date currentDate = new Date();
        rental.setReturnDate(currentDate);
        rental.setLastUpdate(currentDate);
        Rental saved = rentalRepository.save(rental);
        log.info("\"Successfully set returnDate parameter with ID: {}", saved.getRentalId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rentalMapper.toGetDTO(rental));
    }

    public ResponseEntity<Rental> delete(Integer id) {
        log.info("Attempting to delete rental with ID: {}", id);
        Optional<Rental> rental = rentalRepository.findById(id);
        if(rental.isPresent()){
            rentalRepository.delete(rental.get());
            log.info("Successfully deleted rental with ID: {}", id);
            return ResponseEntity.ok().build();      }
        else{
            throw new EntityNotFoundException(ExceptionInfo.ENTITY_RENTAL_NOT_FOUND, id);
        }
    }
}
