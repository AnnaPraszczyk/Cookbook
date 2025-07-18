package com.ania.cookbook.domain.model;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void newProductValidData() {
        UUID id = UUID.randomUUID();
        String name = "test product";
        Product product = Product.newProduct(id, new ProductName(name));

        assertNotNull(product);
        assertEquals(id, product.getProductId());
        assertEquals(name, product.getProductName().name());
    }

    @Test
    void newProductNullId() {
        String name = "test product";
        Exception exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(null, new ProductName(name)));

        assertEquals("Product id cannot be null", exception.getMessage());
    }

    @Test
    void newProductNullName() {
        UUID id = UUID.randomUUID();
        ProductValidationException exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(id, ProductName.from(null)));

        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void newProductBlankName() {
        UUID id = UUID.randomUUID();
        ProductValidationException exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(id,ProductName.from("")));

        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }
}








