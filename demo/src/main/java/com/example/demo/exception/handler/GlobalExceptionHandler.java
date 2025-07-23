package com.example.demo.exception.handler;

import com.example.demo.dto.UniversalExceptionResponseDTO;
import com.example.demo.exception.UniversalException;
import com.example.demo.exception.info.ExceptionInfo;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UniversalExceptionResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            log.error("Validation error in field {}: {}", error.getField(), error.getDefaultMessage());
        });
        UniversalExceptionResponseDTO errorResponse = new UniversalExceptionResponseDTO(
                ExceptionInfo.INPUT_VALIDATION_ERROR.getCode(),
                "Input data validation error",
                ExceptionInfo.INPUT_VALIDATION_ERROR.getStatus().value(),
                Instant.now().toEpochMilli(),
                errors
        );
        return new ResponseEntity<>(errorResponse, ExceptionInfo.INPUT_VALIDATION_ERROR.getStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<UniversalExceptionResponseDTO> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        UniversalExceptionResponseDTO errorResponse = new UniversalExceptionResponseDTO(
                ExceptionInfo.UNEXPECTED_EXCEPTION.getCode(),
                ExceptionInfo.UNEXPECTED_EXCEPTION.getMessage(),
                ExceptionInfo.UNEXPECTED_EXCEPTION.getStatus().value(),
                Instant.now().toEpochMilli()
        );
        return new ResponseEntity<>(errorResponse, ExceptionInfo.UNEXPECTED_EXCEPTION.getStatus());
    }


    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<UniversalExceptionResponseDTO> handleInvalidFormatException(InvalidFormatException ex) {
        if (ex.getTargetType() != null && ex.getTargetType().isAssignableFrom(com.example.demo.model.Gender.class)) {
            log.error("Wrong gender parameter: {}", ex.getMessage(), ex);
            UniversalExceptionResponseDTO errorResponse =  new UniversalExceptionResponseDTO(ExceptionInfo.UNSUPPORTED_GENDER.getCode(),
                    String.format(ExceptionInfo.UNSUPPORTED_GENDER.getMessage(), (String) ex.getValue()),
                    ExceptionInfo.UNSUPPORTED_GENDER.getStatus().value(),
                    Instant.now().toEpochMilli());
            return new ResponseEntity<>(errorResponse, ExceptionInfo.UNSUPPORTED_GENDER.getStatus());
        }
        log.error("Json parsing error: {}", ex.getMessage(), ex);
        UniversalExceptionResponseDTO errorResponse =  new UniversalExceptionResponseDTO(ExceptionInfo.JSON_PARSING_ERROR.getCode(),
                String.format(ExceptionInfo.JSON_PARSING_ERROR.getMessage(), (String) ex.getOriginalMessage()),
                ExceptionInfo.JSON_PARSING_ERROR.getStatus().value(),
                Instant.now().toEpochMilli());
        return new ResponseEntity<>(errorResponse, ExceptionInfo.JSON_PARSING_ERROR.getStatus());
    }


    @ExceptionHandler(UniversalException.class)
    public ResponseEntity<UniversalExceptionResponseDTO> handleUniversalException(UniversalException ex) {
        log.error(ex.getMessage(), ex);
        UniversalExceptionResponseDTO errorResponse = new UniversalExceptionResponseDTO(
                ex.getExceptionInfo().getCode(),
                ex.getMessage(),
                ex.getExceptionInfo().getStatus().value(),
                Instant.now().toEpochMilli()
        );
        return new ResponseEntity<>(errorResponse, ex.getExceptionInfo().getStatus());
    }


}