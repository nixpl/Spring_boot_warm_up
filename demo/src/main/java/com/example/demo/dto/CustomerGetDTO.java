package com.example.demo.dto;

public record CustomerGetDTO(
        Integer customerId,
        String firstName,
        String lastName,
        String email,
        AddressGetDTO address
) {
}
