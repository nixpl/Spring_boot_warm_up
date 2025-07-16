package com.example.demo.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record RentalUpdateDTO(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        Date rentalDate,

        @Min(value = 0, message = "inventoryId cannot be negative")
        Integer inventoryId,

        @Min(value = 0, message = "customerId cannot be negative")
        Integer customerId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        Date returnDate,

        @Min(value = 0, message = "staffId cannot be negative")
        Integer staffId
) {
}
