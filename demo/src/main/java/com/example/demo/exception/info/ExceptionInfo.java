package com.example.demo.exception.info;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionInfo {
    // --- Bad Request - Client Input Errors (100-199) ---
    UNKNOWN_CUSTOMER_FILTER_PARAMETER(100, "Unknown customer filter parameter: %s.", HttpStatus.BAD_REQUEST),
    UNKNOWN_ADDRESS_FILTER_PARAMETER(101, "Unknown address filter parameter: %s.", HttpStatus.BAD_REQUEST),
    UNKNOWN_CITY_FILTER_PARAMETER(102, "Unknown city filter parameter: %s.", HttpStatus.BAD_REQUEST),
    UNKNOWN_COUNTRY_FILTER_PARAMETER(103, "Unknown country filter parameter: %s.", HttpStatus.BAD_REQUEST),
    WRONG_CUSTOMER_FILTER_ARGUMENT(104, "Wrong parameter argument: %s for filter: %s.", HttpStatus.BAD_REQUEST),
    CUSTOMER_EMAIL_TAKEN(105, "Email %s is already in use.", HttpStatus.BAD_REQUEST),
    CUSTOMER_EMAIL_IS_DISPOSABLE(106, "Email %s is detected as temporary. Use permanent email", HttpStatus.BAD_REQUEST),
    INPUT_VALIDATION_ERROR(107, "Validation error in field %s: %s", HttpStatus.BAD_REQUEST),
    ADDRESS_ALREADY_EXISTS(108, "Address with given fields: address = %s, address2 = %s, district = %s, cityId = %d, postalCode = %s, phone = %s already exists", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_GENDER(109, "Given gender is unsupported: %s. Supported are MALE and FEMALE only", HttpStatus.BAD_REQUEST),
    JSON_PARSING_ERROR(110, "Json parsing error: %s", HttpStatus.BAD_REQUEST),
    FILM_IS_NOT_AVAILABLE(111, "This film is already rented by customer: %d", HttpStatus.BAD_REQUEST),

    // --- Not Found - Resource Not Found Errors (200-299) ---
    ENTITY_CUSTOMER_NOT_FOUND(200, "Could not find customer with given id: %d.", HttpStatus.NOT_FOUND),
    ENTITY_ADDRESS_NOT_FOUND(201, "Could not find address with given id: %d.", HttpStatus.NOT_FOUND),
    ENTITY_CITY_NOT_FOUND(202, "Could not find city with given id: %d.", HttpStatus.NOT_FOUND),
    ENTITY_COUNTRY_NOT_FOUND(203, "Could not find country with given id: %d.", HttpStatus.NOT_FOUND),
    ENTITY_RENTAL_NOT_FOUND(204, "Could not find rental with given id: %d.", HttpStatus.NOT_FOUND),
    ENTITY_INVENTORY_NOT_FOUND(204, "Could not find inventory with given id: %d.", HttpStatus.NOT_FOUND),

    // --- Service Unavailable - External Service Errors (300-399) ---
    DISIFY_API_REQUEST_ERROR(300, "Problem caused by DisifyAPI request, error: %s", HttpStatus.SERVICE_UNAVAILABLE),
    GENDERIZE_API_REQUEST_ERROR(301, "Problem caused by GenderizeAPI request, error: %s", HttpStatus.SERVICE_UNAVAILABLE),

    // --- Internal Server Error - Unexpected Errors (500-599) ---
    UNEXPECTED_EXCEPTION(500, "Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    @Getter
    private final HttpStatus status;
    @Getter
    private final Integer code;
    @Getter
    private final String message;

    ExceptionInfo(Integer code, String message, HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}