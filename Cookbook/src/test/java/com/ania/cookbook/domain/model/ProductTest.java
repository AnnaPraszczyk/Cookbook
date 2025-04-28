package com.ania.cookbook.domain.model;

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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Product.newProduct(null, name));
        assertEquals("Product id cannot be null", exception.getMessage());

    }

    @Test
    void testNewProductNullName() {
        UUID id = UUID.randomUUID();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Product.newProduct(id, null));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testGetProductIdAndName() {
        UUID id = UUID.randomUUID();
        String name = "test product";

        Product product = Product.newProduct(id, name);

        assertEquals(id, product.getProductId());
        assertEquals(name, product.getProductName());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        String name = "test product";
        Product product = Product.newProduct(id, name);

        String expected = "Product{name='" + name + "'}";

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




