package com.ania.cookbook.application.services;

import com.ania.cookbook.application.services.ReadProductService;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadProductServiceTest {
    @Mock
    private ReadProduct readProductRepository;
    @InjectMocks
    private ReadProductService readProductService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.newProduct(UUID.randomUUID(), "test product");
    }

    @Test
    void testFindProductById() {
        UUID productId = testProduct.getProductId();
        when(readProductRepository.findProductById(productId)).thenReturn(Optional.of(testProduct));

        Product foundProduct = readProductService.findProductById(productId);

        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getProductId());
        verify(readProductRepository).findProductById(productId);
    }

    @Test
    void testFindProductByIdNotFound() {
        UUID productId = UUID.randomUUID();
        when(readProductRepository.findProductById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> readProductService.findProductById(productId));
        assertEquals("Product not found.", exception.getMessage());
    }

    @Test
    void shouldReturnTrueIfProductExistsById() {
        UUID productId = testProduct.getProductId();
        when(readProductRepository.existsProductById(productId)).thenReturn(true);

        assertTrue(readProductService.existsProductById(productId));
    }

    @Test
    void shouldReturnFalseIfProductDoesNotExistById() {
        UUID productId = UUID.randomUUID();
        when(readProductRepository.existsProductById(productId)).thenReturn(false);

        assertFalse(readProductService.existsProductById(productId));
    }


    @Test
    void testFindProductByName() {
        String productName = testProduct.getProductName();
        when(readProductRepository.findProductByName(productName)).thenReturn(Optional.of(testProduct));

        Product foundProduct = readProductService.findProductByName(productName);

        assertNotNull(foundProduct);
        assertEquals(productName, foundProduct.getProductName());
        verify(readProductRepository).findProductByName(productName);
    }

    @Test
    void testFindProductByNameNotFound() {
        String productName = "nonexistent product";
        when(readProductRepository.findProductByName(productName)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> readProductService.findProductByName(productName));
        assertEquals("Product not found.", exception.getMessage());
    }

    @Test
    void shouldReturnTrueIfProductExistsByName() {
        String productName = testProduct.getProductName();
        when(readProductRepository.existsProductByName(productName)).thenReturn(true);

        assertTrue(readProductService.existsProductByName(productName));
    }

    @Test
    void shouldReturnFalseIfProductDoesNotExistByName() {
        String productName = "Nonexistent Product";
        when(readProductRepository.existsProductByName(productName)).thenReturn(false);

        assertFalse(readProductService.existsProductByName(productName));
    }

}