package com.example.demo.mapper;

import com.example.demo.dto.city.CityCreateDTO;
import com.example.demo.dto.city.CityGetDTO;
import com.example.demo.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {
    @Mapping(target = "lastUpdate", expression = "java(new java.util.Date())")
    City toEntity(CityCreateDTO dto);

    CityGetDTO toGetDTO(City city);
}
