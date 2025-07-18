package com.ania.cookbook.domain.model;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.exceptions.IngredientValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void newIngredient() {
        UUID productId = UUID.randomUUID();
        String productName = "test product";
        Product product =Product.newProduct(productId, new ProductName(productName));
        float amount = 10.0f;
        Unit unit = Unit.KG;
        Ingredient ingredient = Ingredient.newIngredient(product, amount, unit);

        assertNotNull(ingredient);
        assertEquals(productId, ingredient.getProduct().getProductId());
        assertEquals(productName, ingredient.getProduct().getProductName().name());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(unit, ingredient.getUnit());
    }

    @Test
    void newIngredientNullProduct() {
        float amount = 5.0f;
        Unit unit = Unit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(null, amount, unit));
        assertEquals("Product cannot be null", exception.getMessage());
    }

    @Test
    void newIngredientNegativeAmount() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId ,new ProductName("test product"));
        float amount = -2.0f;
        Unit unit = Unit.KG;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, unit));
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void newIngredientNullUnit() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, new ProductName("test product"));
        float amount = 2.0f;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, null));
        assertEquals("Mass unit cannot be null", exception.getMessage());
    }
}


