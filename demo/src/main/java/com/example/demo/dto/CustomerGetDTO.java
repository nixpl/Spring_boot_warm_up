package com.example.demo.dto;

import jakarta.validation.constraints.Email;

public record CustomerGetDTO(
                             String first_name,
                             String last_name,
                             @Email String email,
                             AddressDTO address
) {
}
