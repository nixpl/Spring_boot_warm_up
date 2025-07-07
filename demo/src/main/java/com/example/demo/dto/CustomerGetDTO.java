package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerGetDTO(
        @NotBlank(message = "firstName cannot be empty")
        @Size(max = 45, message = "firstName cannot exceed 45 characters")
        String firstName,

        @NotBlank(message = "lastName cannot be empty")
        @Size(max = 45, message = "lastName cannot exceed 45 characters")
        String lastName,

        @NotBlank(message = "email cannot be empty")
        @Size(max = 45, message = "email cannot exceed 45 characters")
        @Email(message = "Enter correct email")
        String email,

        AddressDTO address
) {
}
