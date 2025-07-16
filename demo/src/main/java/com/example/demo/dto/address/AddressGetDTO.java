package com.example.demo.dto.address;

import com.example.demo.dto.city.CityGetDTO;

public record AddressGetDTO(
        Integer addressId,
        String address,
        String address2,
        String district,
        CityGetDTO city,
        String postalCode,
        String phone) {
}
