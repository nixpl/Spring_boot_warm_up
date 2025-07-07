package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityUpdateDTO(

        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String city,

        Integer countryId) {
}
