package com.example.demo.dto;

public record CustomerCreateDTO(


        Short store_id,


        String first_name,


        String last_name,


        String email,


        Short address_id,


        int active) {
}
