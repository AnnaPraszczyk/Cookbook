package com.ania.cookbook.domain.exceptions;

public class ProductValidationException extends RuntimeException {
    public ProductValidationException(String message) {

        super(message);
    }
}
