package com.example.demo.mapper;

import com.example.demo.dto.AddressCreateDTO;
import com.example.demo.dto.AddressGetDTO;
import com.example.demo.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "city", ignore = true)
    Address toEntity(AddressCreateDTO dto);

//    @Mapping(source = "city.cityId", target = "cityId")
    AddressGetDTO toGetDTO(Address address);
}
