package com.example.demo.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record RentalCreateDTO(

        @NotNull(message = "inventoryId cannot be null")
        @Min(value = 0, message = "inventoryId cannot be negative")
        Integer inventoryId,

        @NotNull(message = "customerId cannot be null")
        @Min(value = 0, message = "customerId cannot be negative")
        Integer customerId,

        @NotNull(message = "staffId cannot be null")
        @Min(value = 0, message = "staffId cannot be negative")
        Integer staffId
) {
}
