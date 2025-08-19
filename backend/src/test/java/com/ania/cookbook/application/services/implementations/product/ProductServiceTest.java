package com.ania.cookbook.application.services.implementations.product;
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
        assertEquals("Test Product", savedProduct.getProductName().name());
        assertTrue(inMemoryRepository.existsProductByName("Test Product"));
    }
    @Test
    void addProductNullName() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(ProductName.from(null)));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void addProductEmptyName() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.addProduct(ProductName.from("")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void addProductWhenExists() {
        ProductName product = ProductName.from("DuplicateProduct");
        Product first = productService.addProduct(product);
        Product second = productService.addProduct(product);
        assertEquals(first.getProductId(), second.getProductId());
    }

    @Test
    void findProductByNameSuccessfully() {
        Product product = Product.newProduct(UUID.randomUUID(),ProductName.from("Test Product"));
        inMemoryRepository.saveProduct(product);
        Optional<Product> result = productService.findProductByName(ProductName.from("Test Product"));
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName().name());
    }

    @Test
    void findProductByNameWhenIsNull() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.findProductByName(ProductName.from(null)));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void findProductByNameWhenIsEmpty() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.findProductByName(ProductName.from("")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void returnTrueWhenProductExists() {
        Product product = Product.newProduct(UUID.randomUUID(), ProductName.from("ExistingProduct"));
        inMemoryRepository.saveProduct(product);

        boolean exists = productService.existsProductByName(ProductName.from("ExistingProduct"));
        assertTrue(exists);
    }

    @Test
    void returnFalseWhenProductIsNull() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.existsProductByName(ProductName.from(null)));

        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }
    @Test

    void returnFalseWhenProductIsEmpty() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.existsProductByName(ProductName.from("")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }
    @Test
    void returnFalseWhenProductDoesNotExist() {
        boolean exists = productService.existsProductByName(ProductName.from("NonExisting"));

        assertFalse(exists);
    }

    @Test
    void updateProductNameSuccessfully() {
        ProductName product = ProductName.from("OldName");
        productService.addProduct(product);
        ProductName newName = ProductName.from("NewName");
        Product updatedProduct = productService.updateProductName(product, newName);

        assertNotNull(updatedProduct);
        assertEquals("NewName", updatedProduct.getProductName().name());
    }

    @Test
    void updateProductWhenNameIsNull() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(ProductName.from(null), ProductName.from("NewName")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateProductWhenNameIsEmpty() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(ProductName.from(""), ProductName.from("Name")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateProductWhenNewNameIsEmpty() {
        ProductName name = new ProductName("OldName");
        productService.addProduct(name);
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.updateProductName(name,ProductName.from("")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }
    @Test
    void updateProductWhenNotExists() {
        ProductName name = ProductName.from("NonExisting");
        ProductName newName = ProductName.from("NewName");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProductName(name, newName));
        assertEquals("Product not found. Unable to update.", exception.getMessage());
    }

    @Test
    void updateProductNameWhenNewAlreadyExists() {
        ProductName sugar = ProductName.from("Sugar");
        ProductName honey = ProductName.from("Honey");
        Product p1 = productService.addProduct(sugar);
        Product p2 = productService.addProduct(honey);
        Product result = productService.updateProductName(sugar, honey);

        assertEquals(p2.getProductId(), result.getProductId());
    }

    @Test
    void removeProductSuccessfully() {
        ProductName product = ProductName.from("ToDelete");
        productService.addProduct(product);
        ProductName deleteProduct = ProductName.from("ToDelete");

        productService.removeProduct(deleteProduct);
        assertFalse(inMemoryRepository.existsProductByName("ToDelete"));
    }

    @Test
    void removeProductWhenIsNull() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(ProductName.from(null)));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());

    }

    @Test
    void removeProductWhenIsEmpty() {
        ProductValidationException exception = assertThrows(ProductValidationException.class,
                () -> productService.removeProduct(ProductName.from("")));
        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void removeProductWhenNotExists() {
        ProductName deleteProduct = new ProductName("NonExisting");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.removeProduct(deleteProduct));
        assertEquals("Unable to delete the product because it does not exist.", exception.getMessage());
    }
}
