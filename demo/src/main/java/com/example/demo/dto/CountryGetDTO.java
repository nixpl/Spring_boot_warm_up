package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record CountryGetDTO(Integer countryId, String country) {
}
