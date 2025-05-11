package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.UUID;

    @Getter
    @Entity
    @Table(name = "product")
    public class ProductEntity {

        @Id
        @GeneratedValue
        @Column(name = "product_id", nullable = false, updatable = false)
        private final UUID productId;
        @Column(name = "product_name", nullable = false, unique = true)
        private final String productName;

        private ProductEntity(UUID productId, String productName) {
            if (productId == null) {
                throw new ProductValidationException("Product id cannot be null");
            }
            this.productId = productId;
            if (productName.isBlank()) {
                throw new ProductValidationException("Product name cannot be null or empty");
            }
            this.productName = productName.trim();
        }

        public ProductEntity() {
            this.productId = UUID.randomUUID();
            this.productName = null;
        }

        public static ProductEntity newProductEntity(UUID productId, String productName) {
            return new ProductEntity(productId, productName);
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
           ProductEntity product = (ProductEntity) o;
           return productId.equals(product.productId);
       }

       @Override
       public int hashCode() {
           return productId.hashCode();
       }

    }



