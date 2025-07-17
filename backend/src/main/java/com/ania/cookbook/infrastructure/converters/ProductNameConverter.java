package com.ania.cookbook.infrastructure.converters;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductNameConverter implements Converter<String, ProductName> {
    @Override
    public ProductName convert(@NonNull String source) {
        return new ProductName(source);
    }
}
