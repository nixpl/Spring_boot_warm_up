package com.example.demo.dto.city;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityCreateDTO(

        @NotBlank(message = "city cannot be empty")
        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String city,

        @NotNull(message = "countryId cannot be null")
        @Min(value = 0, message = "countryId cannot be smaller than 0")
        Integer countryId) {
}
