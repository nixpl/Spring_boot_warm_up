package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        long timestamp,
        Map<String, String> details
) {
    public ErrorResponseDTO(int status, String error, String message, long timestamp) {
        this(status, error, message, timestamp, null);
    }
}