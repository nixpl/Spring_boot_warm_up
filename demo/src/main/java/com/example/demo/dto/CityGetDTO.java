package com.example.demo.dto;

import com.example.demo.model.Country;

public record CityGetDTO(
        Integer cityId,
        String city,
        CountryGetDTO country) {
}