package com.ania.cookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

    @Entity
    @Table(name = "product")
    public class ProductEntity {

        @Id
        @GeneratedValue
        @Column(name="product_id", nullable = false, updatable = false)
        private UUID productId;
        @Column(name="product_name", nullable = false, unique = true)
        private final String productName;


        public ProductEntity() {
            this.productId = UUID.randomUUID();
            this.productName = null;
        }

        private ProductEntity(UUID productId, String productName) {
            if(productId==null){throw new IllegalArgumentException("Product id cannot be null");}
            this.productId = productId;
            if(productName==null || productName.isBlank()){throw new IllegalArgumentException("Product name cannot be null or empty");}
            this.productName = productName.trim().toLowerCase();
        }

        public static ProductEntity newProductEntity(UUID productId, String productName) {
            return new ProductEntity(productId, productName);
        }

        @Override
        public String toString() {
            return "ProductEntity{name='" + productName + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductEntity product = (ProductEntity) o;
            return Objects.equals(productId, product.productId);
        }

        @Override
        public int hashCode() {

            return Objects.hash(productId);
        }

        public UUID getProductId() {

            return productId;
        }

        public String getProductName() {

            return productName;
        }
    }

