package com.ania.cookbook.domain.exceptions;

public class ListValidationException extends RuntimeException {
    public ListValidationException(String message) {
        super(message);
    }
}
