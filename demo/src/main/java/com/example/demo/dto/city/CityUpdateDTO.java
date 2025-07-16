package com.example.demo.dto.city;

import jakarta.validation.constraints.Size;

public record CityUpdateDTO(

        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String city,

        Integer countryId) {
}
