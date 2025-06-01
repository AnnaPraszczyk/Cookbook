package com.ania.cookbook.domain.exceptions;

public class ListNotFoundExeption extends RuntimeException {
    public ListNotFoundExeption(String message) {
        super(message);
    }
}
