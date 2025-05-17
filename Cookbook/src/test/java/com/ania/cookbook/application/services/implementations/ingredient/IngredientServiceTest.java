package com.ania.cookbook.application.services.implementations.ingredient;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientServiceTest {
    private InMemoryProductRepository productRepository;
    private ProductService productService;
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        productService = new ProductService(
                productRepository,
                productRepository,
                productRepository,
                productRepository
        );
        ingredientService = new IngredientService(productService);
    }

    @Test
    void addProductIfNotExists() {
        ProductName productName = new ProductName("Honey");

        assertFalse(productRepository.existsProductByName(productName.name()));

        Product product = productService.addProduct(productName);

        assertTrue(productRepository.existsProductByName(productName.name()));
        assertEquals(productName.name(), product.getProductName());
    }

    @Test
    void createIngredientAndAddProductIfNotExists() {
        ProductName product = new ProductName("Sugar");
        float amount = 200;
        Unit unit = Unit.G;

        Ingredient ingredient = ingredientService.createIngredient(product, amount, unit);

        assertNotNull(ingredient);
        assertEquals(product.name(), ingredient.getProduct().getProductName());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(unit, ingredient.getUnit());
    }
    @Test
    void testCreateIngredient() {
        Ingredient ingredient = ingredientService.createIngredient(new ProductName("Sugar"), 500, Unit.G);
        assertNotNull(ingredient);
        assertEquals("Sugar", ingredient.getProduct().getProductName());
        assertEquals(500, ingredient.getAmount());
    }
}