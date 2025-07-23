package com.example.demo.dto.city;

import com.example.demo.dto.country.CountryGetDTO;

public record CityGetDTO(
        Integer cityId,
        String city,
        CountryGetDTO country) {
}