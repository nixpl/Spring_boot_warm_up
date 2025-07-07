package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record AddressCreateDTO(
        @NotBlank(message = "Address line 1 cannot be empty")
        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String address,

        @Size(max = 50, message = "Address line 2 cannot exceed 50 characters")
        String address2,

        @NotBlank(message = "District cannot be empty")
        @Size(max = 20, message = "District cannot exceed 20 characters")
        String district,

        @NotNull(message = "City cannot be null")
        @Min(value = 0, message = "cityId cannot be negative")
        Integer cityId,

        @NotBlank(message = "Postal code cannot be empty")
        @Size(max = 10, message = "Postal code cannot exceed 10 characters")
        String postalCode,

        @NotBlank(message = "Phone number cannot be empty")
        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        String phone
){}