package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityDTO(

        @NotBlank(message = "city cannot be empty")
        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String city,

        @NotNull(message = "countryId cannot be null")
        @Size(min = 0, message = "countryId cannot be smaller than 0")
        Integer countryId) {
}
