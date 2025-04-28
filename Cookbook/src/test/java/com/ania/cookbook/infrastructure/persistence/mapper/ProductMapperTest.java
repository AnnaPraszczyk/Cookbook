package com.ania.cookbook.infrastructure.persistence.mapper;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void testToDomain() {
        UUID productId = UUID.randomUUID();
        String productName = "test product";
        ProductEntity productEntity = ProductEntity.newProductEntity(productId, productName);

        Product product = ProductMapper.toDomain(productEntity);

        assertNotNull(product);
        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
    }

    @Test
    void testToEntity() {
        UUID productId = UUID.randomUUID();
        String productName = "test product";
        Product product = Product.newProduct(productId, productName);
        ProductEntity productEntity = ProductMapper.toEntity(product);

        assertNotNull(productEntity);
        assertEquals(productId, productEntity.getProductId());
        assertEquals(productName, productEntity.getProductName());
    }



}


