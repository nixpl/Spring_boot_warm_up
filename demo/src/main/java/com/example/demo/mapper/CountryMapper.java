package com.example.demo.mapper;

import com.example.demo.dto.CityCreateDTO;
import com.example.demo.dto.CityGetDTO;
import com.example.demo.dto.CountryCreateDTO;
import com.example.demo.dto.CountryGetDTO;
import com.example.demo.model.City;
import com.example.demo.model.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country toEntity(CountryCreateDTO dto);

    CountryGetDTO toGetDTO(Country country);
}
