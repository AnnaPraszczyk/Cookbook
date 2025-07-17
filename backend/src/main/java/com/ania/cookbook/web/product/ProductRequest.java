package com.ania.cookbook.web.product;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRequest(@JsonProperty("productName") ProductName productName) {
    @JsonCreator
    public ProductRequest {}
}
