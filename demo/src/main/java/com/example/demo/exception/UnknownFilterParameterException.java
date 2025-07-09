package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownFilterParameterException extends RuntimeException {

    public UnknownFilterParameterException(String parameterName) {
        super("Unknown filter parameter: " + parameterName);
    }
}
