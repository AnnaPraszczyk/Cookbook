package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

import static io.micrometer.common.util.StringUtils.isBlank;

public interface ProductUseCase {
    Product addProduct(ProductName productName);
    Optional<Product> findProductByName(ProductName productName);
    boolean existsProductByName(ProductName productName);
    Product updateProductName(ProductName productName, ProductName newName);
    void removeProduct(ProductName productName);


    record ProductName(@JsonProperty("name") String name){
        @JsonCreator
        public ProductName {
            if(isBlank(name)){
                throw new ProductValidationException("Product name cannot be null or empty.");}
        }
    }
}


