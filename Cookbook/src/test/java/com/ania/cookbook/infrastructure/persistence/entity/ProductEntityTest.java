package com.ania.cookbook.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductEntityTest {
    @Test
    void testNewProductEntityValidData() {
        UUID id = UUID.randomUUID();
        String name = "test product";

        ProductEntity product = ProductEntity.newProductEntity(id, name);

        assertNotNull(product);
        assertEquals(id, product.getProductId());
        assertEquals(name, product.getProductName());
    }

    @Test
    void testNewProductEntityNullId() {
        String name = "Invalid Product";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProductEntity.newProductEntity(null, name));

        assertEquals("Product id cannot be null", exception.getMessage());
    }

    @Test
    void testNewProductNullName() {
        UUID id = UUID.randomUUID();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ProductEntity.newProductEntity(id, null));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewProductBlankName() {
        UUID id = UUID.randomUUID();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ProductEntity.newProductEntity(id, ""));
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testGetProductIdAndName() {
        UUID id = UUID.randomUUID();
        String name = "test product";

        ProductEntity product = ProductEntity.newProductEntity(id, name);

        assertEquals(id, product.getProductId());
        assertEquals(name, product.getProductName());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        String name = "string test product";
        ProductEntity product = ProductEntity.newProductEntity(id, name);

        String expected = "ProductEntity{name='" + name + "'}";

        assertEquals(expected, product.toString());
    }

    @Test
    void testEqualsAndHashCodeEqualObjects() {
        UUID id = UUID.randomUUID();
        ProductEntity product1 = ProductEntity.newProductEntity(id, "Test Product");
        ProductEntity product2 = ProductEntity.newProductEntity(id, "Test Product");

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeDifferentObjects() {
        ProductEntity product1 = ProductEntity.newProductEntity(UUID.randomUUID(), "Product A");
        ProductEntity product2 = ProductEntity.newProductEntity(UUID.randomUUID(), "Product B");

        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeDifferentIds() {
        ProductEntity product1 = ProductEntity.newProductEntity(UUID.randomUUID(), "Product A");
        ProductEntity product2 = ProductEntity.newProductEntity(UUID.randomUUID(), "Product A");
        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }
}



