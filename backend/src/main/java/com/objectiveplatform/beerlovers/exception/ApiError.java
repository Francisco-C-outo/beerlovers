package com.objectiveplatform.beerlovers.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Simple DTO to be used in our custom exception handler - should help make our api related errors more readable and helpful
 */
@Getter
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = List.of(error);
    }
}
