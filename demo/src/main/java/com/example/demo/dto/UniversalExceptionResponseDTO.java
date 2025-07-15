package com.example.demo.dto;

import com.example.demo.exception.UniversalException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniversalExceptionResponseDTO(
        int code,
        String message,
        int status,
        long timestamp,
        Map<String, String>details
) {
    public UniversalExceptionResponseDTO(int code, String message, int status, long timestamp) {
        this(code, message, status, timestamp, null);
    }
}
