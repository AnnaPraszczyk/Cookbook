package com.ania.cookbook.application.services.interfaces.conversion;

public interface Converter<T,U> {
    U convert(T source);
}

