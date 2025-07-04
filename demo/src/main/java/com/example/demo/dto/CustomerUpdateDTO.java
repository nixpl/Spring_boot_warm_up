package com.example.demo.dto;

import com.example.demo.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerUpdateDTO( @NotNull(message = "store_id cannot be null")
                                 Short store_id,

                                 @NotBlank(message = "first_name cannot be empty")
                                 @Size(max = 45, message = "first_name cannot exceed 45 characters")
                                 String first_name,

                                 @NotBlank(message = "last_name cannot be empty")
                                 @Size(max = 45, message = "last_name cannot exceed 45 characters")
                                 String last_name,

                                 @NotBlank(message = "email cannot be empty")
                                 @Size(max = 45, message = "email cannot exceed 45 characters")
                                 @Email
                                 String email,

                                 @NotNull(message = "address cannot be null")
                                 Integer address_id,

                                 @NotNull(message = "active cannot be null")
                                 Integer active,

                                Boolean activebool) {}