package com.example.demo.dto;

import com.example.demo.model.Gender;

public record CustomerGetDTO(
        Integer customerId,
        String firstName,
        String lastName,
        Gender gender,
        String email,
        AddressGetDTO address
) {
}
