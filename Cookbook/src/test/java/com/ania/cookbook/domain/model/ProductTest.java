package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void testNewProductValidData() {
        UUID id = UUID.randomUUID();
        String name = "test product";

        Product product = Product.newProduct(id, name);

        assertNotNull(product);
        assertEquals(id, product.getProductId());
        assertEquals(name, product.getProductName());
    }

    @Test
    void testNewProductNullId() {
        String name = "test product";

        Exception exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(null, name));
        assertEquals("Product id cannot be null", exception.getMessage());
    }

    @Test
    void testNewProductNullName() {
        UUID id = UUID.randomUUID();
        ProductValidationException exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(id, null));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewProductBlankName() {
        UUID id = UUID.randomUUID();
        ProductValidationException exception = assertThrows(ProductValidationException.class, () ->
                Product.newProduct(id, ""));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }
}








