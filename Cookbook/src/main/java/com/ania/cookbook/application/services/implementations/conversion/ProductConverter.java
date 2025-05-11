package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<ProductEntity, Product> {

    @Override
    public Product convert(ProductEntity productEntity) {
        if (productEntity == null) throw new ProductValidationException("ProductEntity cannot be null");
        return Product.newProduct(productEntity.getProductId(), productEntity.getProductName());
    }
}
