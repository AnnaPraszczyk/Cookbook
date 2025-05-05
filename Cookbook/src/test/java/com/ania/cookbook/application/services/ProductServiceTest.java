package com.ania.cookbook.application.services;

import com.ania.cookbook.application.services.ProductService;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private SaveProduct saveProductRepository;
    @Mock
    private ReadProduct readProductRepository;
    @Mock
    private UpdateProduct updateProductRepository;
    @Mock
    private DeleteProduct deleteProductRepository;

    @InjectMocks
    private ProductService productService;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.newProduct(UUID.randomUUID(), "test product");
    }

    @Test
    void testSaveProduct() {
        when(readProductRepository.existsProductById(testProduct.getProductId())).thenReturn(false);
        when(saveProductRepository.saveProduct(testProduct)).thenReturn(testProduct);

        Product savedProduct = productService.saveProduct(testProduct);

        assertNotNull(savedProduct);
        assertEquals(testProduct.getProductId(), savedProduct.getProductId());
        verify(saveProductRepository).saveProduct(testProduct);
    }

    @Test
    void testSaveProductThrowsExceptionWhenProductExists() {
        when(readProductRepository.existsProductById(testProduct.getProductId())).thenReturn(true);

        ProductValidationException exception = assertThrows(ProductValidationException.class,() -> productService.saveProduct(testProduct));
        assertEquals("A product with the given id already exists.", exception.getMessage());
    }

    @Test
    void testUpdateProduct() {
        when(readProductRepository.existsProductById(testProduct.getProductId())).thenReturn(true);
        when(updateProductRepository.updateProduct(testProduct)).thenReturn(testProduct);

        Product updatedProduct = productService.updateProduct(testProduct);

        assertNotNull(updatedProduct);
        assertEquals(testProduct.getProductId(), updatedProduct.getProductId());
        verify(updateProductRepository).updateProduct(testProduct);
    }

    @Test
    void testUpdateNonExistingProduct() {
        when(readProductRepository.existsProductById(testProduct.getProductId())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(testProduct));

        assertEquals("Unable to update the product because it does not exist.", exception.getMessage());
    }

    @Test
    void testDeleteProductById() {
        UUID productId = testProduct.getProductId();
        when(readProductRepository.existsProductById(productId)).thenReturn(true);

        productService.deleteProductById(productId);

        verify(deleteProductRepository).deleteProductById(productId);
    }

    @Test
    void testDeleteNonExistingProduct() {
        UUID productId = testProduct.getProductId();
        when(readProductRepository.existsProductById(productId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productService.deleteProductById(productId));

        assertEquals("Unable to delete the product because it does not exist.", exception.getMessage());

    }
}