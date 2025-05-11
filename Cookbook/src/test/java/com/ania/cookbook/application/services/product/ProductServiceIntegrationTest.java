package com.ania.cookbook.application.services.product;
/*
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.AddProductUseCase.AddProduct;
import com.ania.cookbook.application.services.interfaces.product.DeleteProductUseCase.DeleteProductName;
import com.ania.cookbook.application.services.interfaces.product.UpdateProductUseCase.UpdateProductName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceIntegrationTest {\


    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    @Autowired
    private InMemoryProductRepository inMemoryRepository;

    @Test
    void shouldAddProductAndFindIt() {
        AddProduct product = new AddProduct("Test Product");
        productService.addProduct(product);

        Optional<Product> foundProduct = inMemoryRepository.findProductByName("Test Product");

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getProductName());
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateProduct() {
        AddProduct product = new AddProduct("DuplicateProduct");
        productService.addProduct(product);

        assertThrows(ProductValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    void shouldUpdateProductNameSuccessfully() {
        AddProduct product = new AddProduct("OldName");
        productService.addProduct(product);
        UpdateProductName updateProduct = new UpdateProductName("OldName", "NewName");

        Product updatedProduct = productService.updateProductName(updateProduct);

        assertNotNull(updatedProduct);
        assertEquals("NewName", updatedProduct.getProductName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        UpdateProductName updateProduct = new UpdateProductName("NonExisting", "NewName");

        assertThrows(ProductNotFoundException.class, () -> productService.updateProductName(updateProduct));
    }

    @Test
    void shouldRemoveProductSuccessfully() {
        AddProduct product = new AddProduct("ToDelete");
        productService.addProduct(product);
        DeleteProductName deleteProduct = new DeleteProductName("ToDelete");

        productService.removeProduct(deleteProduct);

        assertFalse(inMemoryRepository.existsProductByName("ToDelete"));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        DeleteProductName deleteProduct = new DeleteProductName("NonExisting");

        assertThrows(ProductNotFoundException.class, () -> productService.removeProduct(deleteProduct));
    }*/



