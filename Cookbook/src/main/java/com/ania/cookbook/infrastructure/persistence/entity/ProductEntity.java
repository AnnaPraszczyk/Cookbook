package com.ania.cookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.UUID;

    @Getter
    @Entity
    @Table(name = "product")
    public class Product {

        @Id
        @GeneratedValue
        @Column(name = "product_id", nullable = false, updatable = false)
        private final UUID productId;
        @Column(name = "product_name", nullable = false, unique = true)
        private final String productName;

        private Product(UUID productId, String productName) {
            if (productId == null) {
                throw new IllegalArgumentException("Product id cannot be null");
            }
            this.productId = productId;
            if (productName.isBlank()) {
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }
            this.productName = productName.trim().toLowerCase();
        }

        public Product() {
            this.productId = UUID.randomUUID();
            this.productName = null;
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
           return productId.equals(product.productId);
       }

       @Override
       public int hashCode() {
           return productId.hashCode();
       }

    }



