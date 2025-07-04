package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record CustomerCreateDTO(

        @NotNull(message = "store_id cannot be null")
        @Min(value = 0, message = "store_id cannot be negative")
        Short store_id,

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

        @NotNull(message = "address cannot be null")
        @Min(value = 0, message = "address_id cannot be negative")
        Integer address_id,

        @NotNull(message = "active cannot be null")
        @Min(value = 0, message = "active must be one of {0, 1}")
        @Max(value = 1, message = "active must be one of {0, 1}")
        Integer active) {
}
