package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooManyFiltersException extends RuntimeException{

    public TooManyFiltersException(Set<String> filters) {
        super("Choose only one filter from: " + filters.toString());
    }
}
