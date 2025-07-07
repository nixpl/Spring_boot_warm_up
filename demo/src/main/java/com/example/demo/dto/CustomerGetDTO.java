package com.example.demo.dto;

import jakarta.validation.constraints.Email;

public record CustomerGetDTO(
                             String firstName,
                             String lastName,
                             @Email String email,
                             AddressDTO address
) {
}
