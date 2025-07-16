package com.example.demo.dto.inventory;

import jakarta.validation.constraints.Max;

public record InventoryUpdateDTO(

        @Max(value = 0, message = "inventory_id cannot be negative")
        Short filmId,

        @Max(value = 0, message = "storeId cannot be negative")
        Short storeId
) {
}
