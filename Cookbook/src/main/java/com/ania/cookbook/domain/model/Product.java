package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;

@Getter
public class Product {
    private final UUID productId;
    private final String productName;
    @JsonCreator
    private Product(@JsonProperty("productId")UUID productId,
                    @JsonProperty("productName") String productName) {
        if(productId==null){throw new ProductValidationException("Product id cannot be null");}
        this.productId = productId;
        if(isBlank(productName)){throw new ProductValidationException("Product name cannot be null or empty");}
        this.productName = productName.trim();
    }

    public static Product newProduct(UUID productId, String productName) {
        return new Product(productId, productName);
    }
}

