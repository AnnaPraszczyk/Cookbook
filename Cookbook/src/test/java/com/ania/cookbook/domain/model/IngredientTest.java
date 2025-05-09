package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.IngredientValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void testNewIngredient() {
        UUID productId = UUID.randomUUID();
        String productName = "test product";
        Product product =Product.newProduct(productId, productName);
        float amount = 10.0f;
        MassUnit unit = MassUnit.KG;

        Ingredient ingredient = Ingredient.newIngredient(product, amount, unit);

        assertNotNull(ingredient);
        assertEquals(productId, ingredient.getProduct().getProductId());
        assertEquals(productName, ingredient.getProduct().getProductName());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(unit, ingredient.getMassUnit());
    }

    @Test
    void testNewIngredient2() {

       UUID productId = UUID.randomUUID();
        String productName = "test product";
        Product product =Product.newProduct(productId, productName);
        float amount = 10.0f;
        VolumeUnit unit1= VolumeUnit.CUP;
        float massValue = 120;
        MassUnit unit2 = MassUnit.G;

        Ingredient ingredient = Ingredient.newIngredient(product, amount, unit1, massValue, unit2);

        assertNotNull(ingredient);
        assertEquals(productId, ingredient.getProduct().getProductId());
        assertEquals(productName, ingredient.getProduct().getProductName());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(unit1, ingredient.getVolumeUnit());
        assertEquals(massValue, ingredient.getMassValue());
        assertEquals(unit2, ingredient.getMassUnit());
    }

    @Test
    void testNewIngredientNullProduct() {
        float amount = 5.0f;
        MassUnit unit = MassUnit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(null, amount, unit));
        assertEquals("Product cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredientNullProduct2() {
        float amount = 10.0f;
        VolumeUnit unit1= VolumeUnit.CUP;
        float massValue = 120;
        MassUnit unit2 = MassUnit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(null, amount, unit1, massValue, unit2));
        assertEquals("Product cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredientNegativeAmount() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId ,"test product");
        float amount = -2.0f;
        MassUnit unit = MassUnit.KG;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, unit));
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testNewIngredientNegativeAmount2() {
        Product product = Product.newProduct(UUID.randomUUID() ,"test product");
        float amount = -10.0f;
        VolumeUnit unit1= VolumeUnit.CUP;
        float massValue = 120;
        MassUnit unit2 = MassUnit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, unit1, massValue, unit2));
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testNewIngredientNullMassUnit() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId ,"test product");
        float amount = 2.0f;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, null));
        assertEquals("Mass unit cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredientNullMassUnit2() {
        Product product = Product.newProduct(UUID.randomUUID() ,"test product");
        float amount = 10.0f;
        VolumeUnit unit1= VolumeUnit.CUP;
        float massValue = 120;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, unit1, massValue, null));
        assertEquals("Mass unit cannot be null", exception.getMessage());
    }
    @Test
    void testNewIngredientNullVolumeUnit() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId ,"test product");
        float amount = 2.0f;
        float massValue = 120;
        MassUnit unit2 = MassUnit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, null, massValue, unit2));
        assertEquals("Volume unit cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredientNegativeMassValue () {
        Product product = Product.newProduct(UUID.randomUUID() ,"test product");
        float amount = 10.0f;
        VolumeUnit unit1= VolumeUnit.CUP;
        float massValue = -120;
        MassUnit unit2 = MassUnit.G;

        IngredientValidationException exception = assertThrows(IngredientValidationException.class, () ->
                Ingredient.newIngredient(product, amount, unit1, massValue, unit2));
        assertEquals("Mass value must be greater than 0", exception.getMessage());
    }

}

