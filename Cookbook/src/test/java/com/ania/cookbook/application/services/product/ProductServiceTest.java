package com.ania.cookbook.application.services.product;

import com.ania.cookbook.application.services.interfaces.product.AddProductUseCase.AddProduct;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.DeleteProductUseCase.DeleteProductName;
import com.ania.cookbook.application.services.interfaces.product.UpdateProductUseCase.UpdateProductName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ProductServiceTest {

    private ProductService productService;
    private InMemoryProductRepository inMemoryRepository;

    @BeforeEach
    void setUp() {
        inMemoryRepository = new InMemoryProductRepository();
        productService = new ProductService(inMemoryRepository, inMemoryRepository, inMemoryRepository, inMemoryRepository);
    }

    @Test
    void AddProductSuccessfully() {
        AddProduct newProduct = new AddProduct("Test Product");
        Product savedProduct = productService.addProduct(newProduct);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getProductName());
        assertTrue(inMemoryRepository.existsProductByName("Test Product"));
    }
    @Test
    void AddProductNullName() {
        AddProduct product = new AddProduct(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void AddProductEmptyName() {
        AddProduct product = new AddProduct("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void AddProductWhenExists() {
        AddProduct product = new AddProduct("DuplicateProduct");
        productService.addProduct(product);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("A product already exists.", exception.getMessage());
    }

    @Test
    void UpdateProductNameSuccessfully() {
        AddProduct product = new AddProduct("OldName");
        productService.addProduct(product);
        UpdateProductName updateProduct = new UpdateProductName("OldName", "NewName");

        Product updatedProduct = productService.updateProductName(updateProduct);

        assertNotNull(updatedProduct);
        assertEquals("NewName", updatedProduct.getProductName());
    }

    @Test
    void UpdateProductWhenNameIsNull() {
        UpdateProductName updateProduct = new UpdateProductName(null, "NewName");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(updateProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void UpdateProductWhenNameIsEmpty() {
        UpdateProductName updateProduct = new UpdateProductName("", "NewName");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(updateProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }
    @Test
    void UpdateProductWhenNewNameIsNull() {
        UpdateProductName updateProduct = new UpdateProductName("OldName", null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(updateProduct));
        assertEquals("New product name cannot be null or empty.", exception.getMessage());
    }
    @Test
    void UpdateProductWhenNewNameIsEmpty() {
        UpdateProductName updateProduct = new UpdateProductName("OldName", "");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(updateProduct));
        assertEquals("New product name cannot be null or empty.", exception.getMessage());
    }
    @Test
    void UpdateProductWhenNotExists() {
        UpdateProductName updateProduct = new UpdateProductName("NonExisting", "NewName");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProductName(updateProduct));
        assertEquals("Product not found. Unable to update.", exception.getMessage());
    }

    @Test
    void RemoveProductSuccessfully() {
        AddProduct product = new AddProduct("ToDelete");
        productService.addProduct(product);
        DeleteProductName deleteProduct = new DeleteProductName("ToDelete");

        productService.removeProduct(deleteProduct);
        assertFalse(inMemoryRepository.existsProductByName("ToDelete"));
    }

    @Test
    void RemoveProductWhenIsNull() {
        DeleteProductName deleteProduct = new DeleteProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());

    }

    @Test
    void RemoveProductWhenIsEmpty() {
        DeleteProductName deleteProduct = new DeleteProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void RemoveProductWhenNotExists() {
        DeleteProductName deleteProduct = new DeleteProductName("NonExisting");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Unable to delete the product because it does not exist.", exception.getMessage());
    }
}
