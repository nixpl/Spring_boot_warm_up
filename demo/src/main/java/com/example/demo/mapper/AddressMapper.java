package com.example.demo.mapper;

import com.example.demo.dto.address.AddressCreateDTO;
import com.example.demo.dto.address.AddressGetDTO;
import com.example.demo.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "lastUpdate", expression = "java(new java.util.Date())")
    Address toEntity(AddressCreateDTO dto);

//    @Mapping(source = "city.cityId", target = "cityId")
    AddressGetDTO toGetDTO(Address address);
}
