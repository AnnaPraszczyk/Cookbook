package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityConverter implements Converter<Product, ProductEntity> {
    @Override
    public ProductEntity convert(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        return ProductEntity.newProductEntity(product.getProductId(), product.getProductName());
    }
}

