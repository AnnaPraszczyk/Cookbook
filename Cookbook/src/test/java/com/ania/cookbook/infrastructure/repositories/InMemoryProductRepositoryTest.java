package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
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
        ProductEntity product = ProductEntity.newProductEntity(productId, "Sugar");

        ProductEntity savedProduct = productRepository.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals(productId, savedProduct.getProductId());
        assertEquals("sugar", savedProduct.getProductName());
        assertTrue(productRepository.existsProductById(productId));
    }

    @Test
    void testSaveProduct_AlreadyExists() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(productId, "Sugar");

        productRepository.saveProduct(product);

        assertThrows(IllegalArgumentException.class, () ->
                productRepository.saveProduct(product)
        );
    }

    @Test
    void testFindProductById() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(productId, "Sugar");

        productRepository.saveProduct(product);

        Optional<ProductEntity> foundProduct = productRepository.findProductById(productId);

        assertTrue(foundProduct.isPresent());
        assertEquals(productId, foundProduct.get().getProductId());
        assertEquals("sugar", foundProduct.get().getProductName());
    }

    @Test
    void testFindProductById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<ProductEntity> foundProduct = productRepository.findProductById(nonExistentId);

        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testFindProductById_Empty() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<ProductEntity> foundProduct = productRepository.findProductById(nonExistentId);

        assertTrue(foundProduct.isEmpty());
    }

    @Test
    void testExistsProductById() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(productId, "Sugar");

        productRepository.saveProduct(product);

        assertTrue(productRepository.existsProductById(productId));
    }

    @Test
    void testExistsProductById_ProductDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        assertFalse(productRepository.existsProductById(nonExistentId));
    }

    @Test
    void testExistsProductById_False() {
        UUID nonExistentId = UUID.randomUUID();

        assertFalse(productRepository.existsProductById(nonExistentId));
    }

    @Test
    void testFindProductByName_Success() {
        String productName = "sugar";
        ProductEntity product = ProductEntity.newProductEntity(UUID.randomUUID(), productName);

        productRepository.saveProduct(product);

        ProductEntity foundProduct = productRepository.findProductByName(productName);

        assertNotNull(foundProduct);
        assertEquals(productName, foundProduct.getProductName());
    }

    @Test
    void testFindProductByName_NotFound() {
        String nonExistentName = "Salt";

        ProductEntity foundProduct = productRepository.findProductByName(nonExistentName);

        assertNull(foundProduct);
    }

    @Test
    void testFindProductByName_NullName() {
        ProductEntity foundProduct = productRepository.findProductByName(null);

        assertNull(foundProduct);
    }

    @Test
    void testExistsProductByName_ProductExists() {
        String productName = "sugar";

        productRepository.saveProduct(ProductEntity.newProductEntity(UUID.randomUUID(), productName));

        assertTrue(productRepository.existsProductByName(productName));
    }

    @Test
    void testExistsProductByName_ProductDoesNotExist() {
        String nonExistentName = "Salt";

        assertFalse(productRepository.existsProductByName(nonExistentName));
    }

    @Test
    void testUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(id, "product");
        productRepository.saveProduct(product);

        ProductEntity updatedProduct = ProductEntity.newProductEntity(id,"butter updated");
        productRepository.updateProduct(updatedProduct);

        Optional<ProductEntity> foundProduct = productRepository.findProductById(updatedProduct.getProductId());

        assertTrue(foundProduct.isPresent());
        assertEquals("butter updated", foundProduct.get().getProductName());
    }

    @Test
    void testDeleteProductById() {
        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(id,"Salt");
        productRepository.saveProduct(product);

        productRepository.deleteProductById(product.getProductId());

        assertFalse(productRepository.existsProductById(product.getProductId()));
        assertTrue(productRepository.findProductById(product.getProductId()).isEmpty());
    }
}