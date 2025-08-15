package com.ania.cookbook.domain.exceptions;

public class RecipeDeleteException extends RuntimeException {
    public RecipeDeleteException(String message) {
        super(message);
    }
}
