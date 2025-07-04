package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerGetDTO(
        @NotBlank(message = "first_name cannot be empty")
        @Size(max = 45, message = "first_name cannot exceed 45 characters")
        String first_name,

        @NotBlank(message = "last_name cannot be empty")
        @Size(max = 45, message = "last_name cannot exceed 45 characters")
        String last_name,

        @NotBlank(message = "email cannot be empty")
        @Size(max = 45, message = "email cannot exceed 45 characters")
        @Email(message = "Enter correct email")
        String email,

        AddressDTO address
) {
}
