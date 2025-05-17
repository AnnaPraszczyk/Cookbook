package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
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
    void addProductSuccessfully() {
        ProductName newProduct = new ProductName("Test Product");
        Product savedProduct = productService.addProduct(newProduct);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getProductName());
        assertTrue(inMemoryRepository.existsProductByName("Test Product"));
    }
    @Test
    void addProductNullName() {
        ProductName product = new ProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void addProductEmptyName() {
        ProductName product = new ProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void addProductWhenExists() {
        ProductName product = new ProductName("DuplicateProduct");
        productService.addProduct(product);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(product));
        assertEquals("A product already exists.", exception.getMessage());
    }

    @Test
    void findProductByNameSuccessfully() {
        Product product = Product.newProduct(UUID.randomUUID(), "Test Product");
        inMemoryRepository.saveProduct(product);

        Optional<Product> result = productService.findProductByName(new ProductName("Test Product"));

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    @Test
    void findProductByNameWhenIsNull() {
        ProductName productName = new ProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.findProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void findProductByNameWhenIsEmpty() {
        ProductName productName = new ProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.findProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void findProductByNameWhenProductNotExist() {
        ProductName productName = new ProductName("NonExisting");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.findProductByName(productName));
        assertEquals("Unable to find the product because it does not exist.", exception.getMessage());
    }

    @Test
    void returnTrueWhenProductExists() {
        Product product = Product.newProduct(UUID.randomUUID(), "ExistingProduct");
        inMemoryRepository.saveProduct(product);

        boolean exists = productService.existsProductByName(new ProductName("ExistingProduct"));
        assertTrue(exists);
    }

    @Test
    void returnFalseWhenProductIsNull() {
        ProductName productName = new ProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.existsProductByName(productName));


        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }
    @Test

    void returnFalseWhenProductIsEmpty() {

        ProductName productName = new ProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.existsProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }
    @Test
    void returnFalseWhenProductDoesNotExist() {
        boolean exists = productService.existsProductByName(new ProductName("NonExisting"));

        assertFalse(exists);
    }

    @Test
    void updateProductNameSuccessfully() {
        ProductName product = new ProductName("OldName");
        productService.addProduct(product);
        ProductName newName = new ProductName("NewName");
        Product updatedProduct = productService.updateProductName(product, newName);

        assertNotNull(updatedProduct);
        assertEquals("NewName", updatedProduct.getProductName());
    }

    @Test
    void updateProductWhenNameIsNull() {
        ProductName name = new ProductName("Name");
        ProductName newName = new ProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(name, newName));
        assertEquals("New product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateProductWhenNameIsEmpty() {
        ProductName name = new ProductName("");
        ProductName newName = new ProductName("Name");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(name,newName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateProductWhenNewNameIsEmpty() {
        ProductName name = new ProductName("OldName");
        ProductName newName = new ProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(name, newName));
        assertEquals("New product name cannot be null or empty.", exception.getMessage());
    }
    @Test
    void updateProductWhenNotExists() {
        ProductName name = new ProductName("NonExisting");
        ProductName newName = new ProductName("NewName");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProductName(name, newName));
        assertEquals("Product not found. Unable to update.", exception.getMessage());
    }

    @Test
    void removeProductSuccessfully() {
        ProductName product = new ProductName("ToDelete");
        productService.addProduct(product);
        ProductName deleteProduct = new ProductName("ToDelete");

        productService.removeProduct(deleteProduct);
        assertFalse(inMemoryRepository.existsProductByName("ToDelete"));
    }

    @Test
    void removeProductWhenIsNull() {
        ProductName deleteProduct = new ProductName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());

    }

    @Test
    void removeProductWhenIsEmpty() {
        ProductName deleteProduct = new ProductName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void removeProductWhenNotExists() {
        ProductName deleteProduct = new ProductName("NonExisting");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Unable to delete the product because it does not exist.", exception.getMessage());
    }
}
