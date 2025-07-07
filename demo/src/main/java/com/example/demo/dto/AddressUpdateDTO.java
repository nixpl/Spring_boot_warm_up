package com.example.demo.dto;

import jakarta.validation.constraints.Size;

public record AddressUpdateDTO(

        @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
        String address,

        @Size(max = 50, message = "Address line 2 cannot exceed 50 characters")
        String address2,

        @Size(max = 20, message = "District cannot exceed 20 characters")
        String district,

        Integer cityId,

        @Size(max = 10, message = "Postal code cannot exceed 10 characters")
        String postalCode,

        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        String phone
){}