package com.example.demo.mapper;

import com.example.demo.dto.country.CountryCreateDTO;
import com.example.demo.dto.country.CountryGetDTO;
import com.example.demo.model.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    @Mapping(target = "lastUpdate", expression = "java(new java.util.Date())")
    Country toEntity(CountryCreateDTO dto);

    CountryGetDTO toGetDTO(Country country);
}
