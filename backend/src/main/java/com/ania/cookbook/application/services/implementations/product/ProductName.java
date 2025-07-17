package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static io.micrometer.common.util.StringUtils.isBlank;

@Getter
@EqualsAndHashCode
public final class ProductName{
    private final String name;

    public ProductName(String name) {
        if(isBlank(name)) throw new ProductValidationException("Product name cannot be null or empty.");
        this.name = name;
    }

    @JsonCreator
    public static ProductName from(String name){
        if(isBlank(name)){
            throw new ProductValidationException("Product name cannot be null or empty.");
        }
        return new ProductName(name);
    }

    @JsonValue
    public String value() {
        return name;
    }

    public String name() {
        return name;
    }
}

