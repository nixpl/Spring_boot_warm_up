package com.example.demo.dto.customer;

import com.example.demo.dto.address.AddressGetDTO;
import com.example.demo.model.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;

public record CustomerGetDTO(
        Integer customerId,
        String firstName,
        String lastName,
        Gender gender,
        String email,
        AddressGetDTO address
) {
}
