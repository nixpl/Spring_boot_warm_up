package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record AddressDTO(
        @NotBlank(message = "Address line 1 cannot be empty")
        @Pattern(regexp = "^\\d+\\s(?:[A-Z][a-z]*\\s)*[A-Z][a-z]*$",
                message = "The address format is invalid. Expected format: 'Number Street_Name Street_Type")
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
        @Pattern(regexp = "^[0-9-]*$", message = "Postal code can only contain digits and hyphens, without spaces")
        String postalCode,

        @NotBlank(message = "Phone number cannot be empty")
        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        @Pattern(regexp = "^\\+?\\d*$", message = "Phone number can only contain digits and an optional leading plus sign.")
        String phone
){}