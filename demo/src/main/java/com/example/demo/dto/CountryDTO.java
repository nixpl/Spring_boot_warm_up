package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record CountryDTO(@NotBlank String country) {
}
