package com.ania.cookbook.domain.exceptions;

public class IngredientValidationException extends RuntimeException {
    public IngredientValidationException(String message) {
        super(message);
    }
}
