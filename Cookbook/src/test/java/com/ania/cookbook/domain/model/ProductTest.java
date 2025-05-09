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

    @Test
    void testToString() {
        UUID productId = UUID.randomUUID();
        String productName = "test product";
        Product product = Product.newProduct(productId, productName);

        String expected = "Product{productId=" + productId + ", productName='" + productName + "'}";
        assertEquals(expected, product.toString());
    }
    @Test
    void testEqualsAndHashCodeEqualObjects() {
        UUID id = UUID.randomUUID();
        Product product1 = Product.newProduct(id, "test product");
        Product product2 = Product.newProduct(id, "test product");

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }
    @Test
    void testEqualsAndHashCodeDifferentObjects() {
        Product product1 = Product.newProduct(UUID.randomUUID(), "Product A");
        Product product2 = Product.newProduct(UUID.randomUUID(), "Product B");

        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }
    @Test
    void testEqualsAndHashCodeDifferentIds() {
        Product product1 = Product.newProduct(UUID.randomUUID(), "Product A");
        Product product2 = Product.newProduct(UUID.randomUUID(), "Product A");
        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }

}




