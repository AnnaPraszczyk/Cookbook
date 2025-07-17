package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductEntity toEntity(Product domain) {
        return ProductEntity.newProductEntity(domain.getProductId(), domain.getProductName().name());
    }

    public Product toDomain(ProductEntity entity) {
        if (entity.getProductName() == null) {
            System.err.println("❌ ProductEntity has null name: " + entity.getProductId());
        }
        return Product.newProduct(
                entity.getProductId(),
                new ProductName(entity.getProductName())
        );
    }
}
