package com.example.demo.dto.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record InventoryCreateDTO(

        @NotNull(message = "inventoryId cannot be null")
        @Max(value = 0, message = "inventory_id cannot be negative")
        Short filmId,

        @NotNull(message = "storeId cannot be null")
        @Max(value = 0, message = "storeId cannot be negative")
        Short storeId
) {
}
