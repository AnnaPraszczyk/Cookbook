package com.ania.cookbook.domain.exceptions;

public class RatingValidationException extends RuntimeException {
    public RatingValidationException(String message) {
        super(message);
    }
}
