package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import java.util.function.Function;

public class InMemoryConverter<S, T> implements Converter<S, T>{
    private final Function<S, T> conversionLogic;

    public InMemoryConverter(Function<S, T> conversionLogic) {
        this.conversionLogic = conversionLogic;
    }

    @Override
    public T convert(S source) {
        if (source == null) {
            throw new IllegalArgumentException("Source object cannot be null");
        }
        return conversionLogic.apply(source);
    }
}
