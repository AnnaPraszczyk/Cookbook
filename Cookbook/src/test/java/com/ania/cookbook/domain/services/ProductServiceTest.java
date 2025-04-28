package com.ania.cookbook.domain.services;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService productService;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(InMemoryProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void testSaveProduct() {
        UUID id = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(id, "Test Product");

        when(productRepository.existsProductById(id)).thenReturn(false);
        when(productRepository.saveProduct(productEntity)).thenReturn(productEntity);

        ProductEntity savedProduct = productService.saveProduct(productEntity);

        assertNotNull(savedProduct);
        assertEquals(productEntity, savedProduct);
        verify(productRepository, times(1)).existsProductById(id);
        verify(productRepository, times(1)).saveProduct(productEntity);
    }


    @Test
    void testSaveProductThrowsExceptionWhenProductExists() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(productId, "Test Product");

        when(productRepository.existsProductById(productId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(product));
        assertEquals("A product with the given ID already exists.", exception.getMessage());
        verify(productRepository, times(1)).existsProductById(productId);
        verify(productRepository, never()).saveProduct(product);
    }

    @Test
    void testUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(id, "Updated Product");

        when(productRepository.existsProductById(id)).thenReturn(true);
        when(productRepository.updateProduct(productEntity)).thenReturn(productEntity);

        ProductEntity updatedProduct = productService.updateProduct(productEntity);

        assertNotNull(updatedProduct);
        assertEquals(productEntity, updatedProduct);
        verify(productRepository, times(1)).existsProductById(id);
        verify(productRepository, times(1)).updateProduct(productEntity);
    }

    @Test
    void testUpdateNonExistingProduct() {
        UUID id = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(id, "Non-Existent Product");

        when(productRepository.existsProductById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.updateProduct(productEntity));

        assertEquals("Unable to update the product because it does not exist.", exception.getMessage());
        verify(productRepository, times(1)).existsProductById(id);
        verify(productRepository, never()).updateProduct(productEntity);
    }

    @Test
    void testDeleteProductById() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsProductById(id)).thenReturn(true);
        doNothing().when(productRepository).deleteProductById(id);

        productService.deleteProductById(id);

        verify(productRepository, times(1)).existsProductById(id);
        verify(productRepository, times(1)).deleteProductById(id);
    }

    @Test
    void testDeleteNonExistingProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsProductById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.deleteProductById(id));

        assertEquals("Unable to delete the product because it does not exist.", exception.getMessage());
        verify(productRepository, times(1)).existsProductById(id);
        verify(productRepository, never()).deleteProductById(id);
    }
}