package com.example.demo.dto;

public record CustomerUpdateDTO(Short store_id,
                                String first_name,
                                String last_name,
                                String email,
                                Short address_id,
                                Integer active,
                                Boolean activebool) {}