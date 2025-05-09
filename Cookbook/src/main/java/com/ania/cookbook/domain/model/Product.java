package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import lombok.Getter;
import java.util.Objects;
import java.util.UUID;

@Getter

public class Product {
    private final UUID productId;
    private final String productName;

    public Product() {
        this.productId = null;
        this.productName = null;
    }

    private Product(UUID productId, String productName) {
        if(productId==null){throw new ProductValidationException("Product id cannot be null");}
        this.productId = productId;
        if(productName==null || productName.isBlank()){throw new ProductValidationException("Product name cannot be null or empty");}
        this.productName = productName.trim().toLowerCase();
    }

    public static Product newProduct(UUID productId, String productName) {
        return new Product(productId, productName);
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
