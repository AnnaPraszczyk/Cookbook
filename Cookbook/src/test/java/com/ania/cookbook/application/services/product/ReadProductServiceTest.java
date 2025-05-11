package com.ania.cookbook.application.services.product;

import com.ania.cookbook.application.services.implementations.product.ReadProductService;
import com.ania.cookbook.application.services.interfaces.product.FindProductUseCase.FindProductByName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ReadProductServiceTest {
    private ReadProductService readProductService;
    private InMemoryProductRepository inMemoryRepository;

    @BeforeEach
    void setUp() {
        inMemoryRepository = new InMemoryProductRepository();
        readProductService = new ReadProductService(inMemoryRepository);
    }

    @Test
    void FindProductByNameSuccessfully() {
        Product product = Product.newProduct(UUID.randomUUID(), "Test Product");
        inMemoryRepository.saveProduct(product);

        Optional<Product> result = readProductService.findProductByName(new FindProductByName("Test Product"));

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    @Test
    void FindProductByNameWhenIsNull() {
        FindProductByName productName = new FindProductByName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> readProductService.findProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void FindProductByNameWhenIsEmpty() {
        FindProductByName productName = new FindProductByName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> readProductService.findProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }

    @Test
    void FindProductByNameWhenProductNotExist() {
        FindProductByName productName = new FindProductByName("NonExisting");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> readProductService.findProductByName(productName));
        assertEquals("Unable to find the product because it does not exist.", exception.getMessage());
    }

    @Test
    void ReturnTrueWhenProductExists() {
        Product product = Product.newProduct(UUID.randomUUID(), "ExistingProduct");
        inMemoryRepository.saveProduct(product);

        boolean exists = readProductService.existsProductByName(new FindProductByName("ExistingProduct"));
        assertTrue(exists);
    }

    @Test
    void ReturnFalseWhenProductIsNull() {
        FindProductByName productName = new FindProductByName(null);

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> readProductService.existsProductByName(productName));


        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }
    @Test

    void ReturnFalseWhenProductIsEmpty() {

        FindProductByName productName = new FindProductByName("");

        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> readProductService.existsProductByName(productName));
        assertEquals("Product cannot be null or empty.", exception.getMessage());
    }
    @Test
    void ReturnFalseWhenProductDoesNotExist() {
        boolean exists = readProductService.existsProductByName(new FindProductByName("NonExisting"));

        assertFalse(exists);
    }

}


