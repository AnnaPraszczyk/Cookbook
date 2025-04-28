package com.ania.cookbook.infrastructure.persistence.mapper;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;

public class ProductMapper {

    public static Product toDomain(ProductEntity entity) {
        if(entity == null) {throw new IllegalArgumentException("ProductEntity cannot be null");}
        return Product.newProduct(entity.getProductId(), entity.getProductName());
    }

    public static ProductEntity toEntity(Product domain) {
        if(domain == null) {throw new IllegalArgumentException("Product cannot be null!");}

        return ProductEntity.newProductEntity(domain.getProductId(), domain.getProductName());
    }
}