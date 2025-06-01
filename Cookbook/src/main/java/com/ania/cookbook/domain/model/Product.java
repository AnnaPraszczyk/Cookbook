package com.ania.cookbook.domain.model;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Product {
    private final UUID productId;
    private final ProductName productName;
    @JsonCreator
    private Product(@JsonProperty("productId")UUID productId,
                    @JsonProperty("productName") ProductName productName) {
        if(productId==null){throw new ProductValidationException("Product id cannot be null");}
        this.productId = productId;
        this.productName = productName;
    }

    public static Product newProduct(UUID productId, ProductName productName) {
        return new Product(productId, productName);
    }
}

