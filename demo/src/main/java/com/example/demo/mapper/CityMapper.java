package com.example.demo.mapper;

import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City toEntity(CityCreateDTO dto);

    CityGetDTO toGetDTO(City city);
}
