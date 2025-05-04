package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    void testSaveProduct() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, "sugar");

        Product savedProduct = productRepository.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
        assertTrue(productRepository.existsProductById(productId));
    }

    @Test
    void testSaveProductAlreadyExists() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, "sugar");

        productRepository.saveProduct(product);

        assertThrows(ProductValidationException.class, () ->
                productRepository.saveProduct(product)
        );
    }

    @Test
    void testFindProductById() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, "orange");

        productRepository.saveProduct(product);

        Optional<Product> foundProduct = productRepository.findProductById(product.getProductId());

        assertTrue(foundProduct.isPresent());
        assertEquals(product.getProductId(), foundProduct.get().getProductId());
        assertEquals("orange", foundProduct.get().getProductName());
    }

    @Test
    void testFindProductByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Product> foundProduct = productRepository.findProductById(nonExistentId);
        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void testFindProductByIdEmpty() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<Product> foundProduct = productRepository.findProductById(nonExistentId);

        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void testExistsProductById() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, "grapes");

        productRepository.saveProduct(product);

        assertTrue(productRepository.existsProductById(product.getProductId()));
    }

    @Test
    void testExistsProductByIdProductDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        assertFalse(productRepository.existsProductById(nonExistentId));
    }

    @Test
    void testExistsProductByIdFalse() {
        UUID nonExistentId = UUID.randomUUID();

        assertFalse(productRepository.existsProductById(nonExistentId));
    }

    @Test
    void testFindProductByName() {
        String productName = "sugar";
        Product product = Product.newProduct(UUID.randomUUID(), productName);

        productRepository.saveProduct(product);

        Optional<Product> foundProduct = productRepository.findProductByName(productName);

        assertTrue(foundProduct.isPresent());
        assertEquals(productName, foundProduct.get().getProductName());
    }

    @Test
    void testFindProductByNameNotFound() {
        String nonExistentName = "salt";

        Optional<Product> foundProduct = productRepository.findProductByName(nonExistentName);

        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void testFindProductByNameNullName() {
        Optional<Product> foundProduct = productRepository.findProductByName(null);

        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void testExistsProductByNameProductExists() {
        String productName = "sugar";

        productRepository.saveProduct(Product.newProduct(UUID.randomUUID(), productName));

        assertTrue(productRepository.existsProductByName(productName));
    }

    @Test
    void testExistsProductByNameProductDoesNotExist() {
        String nonExistentName = "salt";

        assertFalse(productRepository.existsProductByName(nonExistentName));
    }

    @Test
    void testUpdateProduct() {
        UUID id = UUID.randomUUID();
        Product product = Product.newProduct(id, "product");
        productRepository.saveProduct(product);

        Product updatedProduct = Product.newProduct(id,"butter updated");
        productRepository.updateProduct(updatedProduct);

        Optional<Product> foundProduct = productRepository.findProductById(updatedProduct.getProductId());

        assertTrue(foundProduct.isPresent());
        assertEquals("butter updated", foundProduct.get().getProductName());
    }

    @Test
    void testDeleteProductById() {
        UUID id = UUID.randomUUID();
        Product product = Product.newProduct(id,"salt");
        productRepository.saveProduct(product);

        productRepository.deleteProductById(product.getProductId());

        assertFalse(productRepository.existsProductById(product.getProductId()));
        assertTrue(productRepository.findProductById(product.getProductId()).isEmpty());
    }
}