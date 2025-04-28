package com.ania.cookbook.domain.services;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadProductServiceTest {

    private ReadProductService readProductService;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(InMemoryProductRepository.class);
        readProductService = new ReadProductService(productRepository);
    }

    @Test
    void testFindProductById() {
        UUID id = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(id, "Test Product");

        when(productRepository.findProductById(id)).thenReturn(Optional.of(productEntity));

        Optional<ProductEntity> foundProduct = readProductService.findProductById(id);

        assertTrue(foundProduct.isPresent());
        assertEquals(productEntity, foundProduct.get());
        verify(productRepository, times(1)).findProductById(id);
    }

    @Test
    void testFindProductByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(productRepository.findProductById(id)).thenReturn(Optional.empty());

        Optional<ProductEntity> foundProduct = readProductService.findProductById(id);

        assertFalse(foundProduct.isPresent());
        verify(productRepository, times(1)).findProductById(id);
    }

    @Test
    void testFindProductByName() {
        String name = "Test Product";
        ProductEntity productEntity = ProductEntity.newProductEntity(UUID.randomUUID(), name);

        when(productRepository.findProductByName(name)).thenReturn(productEntity);

        Optional<ProductEntity> foundProduct = readProductService.findProductByName(name);

        assertTrue(foundProduct.isPresent());
        assertEquals(productEntity, foundProduct.get());
        verify(productRepository, times(1)).findProductByName(name);
    }

    @Test
    void testFindProductByNameNotFound() {
        String name = "Nonexistent Product";

        when(productRepository.findProductByName(name)).thenReturn(null);

        Optional<ProductEntity> foundProduct = readProductService.findProductByName(name);

        assertFalse(foundProduct.isPresent());
        verify(productRepository, times(1)).findProductByName(name);
    }
}