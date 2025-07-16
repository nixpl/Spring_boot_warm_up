package com.example.demo.mapper;

import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.dto.rental.RentalCreateDTO;
import com.example.demo.dto.rental.RentalGetDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "rentalDate", expression = "java(new java.util.Date())")
    @Mapping(target = "lastUpdate", expression = "java(new java.util.Date())")
    Rental toEntity(RentalCreateDTO dto);

    @Mapping(source = "inventory.inventoryId", target = "inventoryId")
    @Mapping(source = "customer.customerId", target = "customerId")
    RentalGetDTO toGetDTO(Rental rental);
}
