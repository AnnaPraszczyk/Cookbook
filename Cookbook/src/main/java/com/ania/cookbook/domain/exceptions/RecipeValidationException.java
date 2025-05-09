package com.ania.cookbook.domain.exceptions;

public class RecipeValidationException extends RuntimeException {
    public RecipeValidationException(String message) {

        super(message);
    }
}
