package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityDTO(

        @NotBlank(message = "city cannot be empty")
        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String city,

        @NotNull(message = "country_id cannot be null")
        @Size(min = 0, message = "country_id cannot be smaller than 0")
        Integer country_id) {
}
