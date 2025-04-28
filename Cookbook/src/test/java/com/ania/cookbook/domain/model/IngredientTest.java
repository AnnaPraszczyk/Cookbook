package com.ania.cookbook.domain.model;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void testNewIngredient_Success() {
        Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
        ProductEntity productEntity = null;
        Unit unit = Unit.KG;
        UnitValue unitValue = null;
        float amount = 10.0f;

        Ingredient ingredient = Ingredient.newIngredient(product, productEntity, amount, unit, unitValue);

        assertNotNull(ingredient);
        assertEquals(Optional.of(product), ingredient.getProduct());
        assertEquals(Optional.empty(), ingredient.getProductEntity());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(Optional.of(unit), ingredient.getUnit());
        assertEquals(Optional.empty(), ingredient.getUnitValue());
    }

    @Test
    void testNewIngredient2_Success() {
        Product product = null;
        UUID productEntityId = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(productEntityId,"Test Product");
        Unit unit = null;
        UnitValue unitValue = UnitValue.newUnitValue(UUID.randomUUID(), productEntityId, Unit.CUP, 10.0f);
        float amount = 10.0f;

        Ingredient ingredient = Ingredient.newIngredient(product, productEntity, amount, unit, unitValue);

        assertNotNull(ingredient);
        assertEquals(Optional.empty(), ingredient.getProduct());
        assertEquals(Optional.of(productEntity), ingredient.getProductEntity());
        assertEquals(amount, ingredient.getAmount());
        assertEquals(Optional.empty(), ingredient.getUnit());
        assertEquals(Optional.of(unitValue), ingredient.getUnitValue());
    }

    @Test
    void testNewIngredient_NotSuccess() {
        Product product = null;
        UUID productEntityId = UUID.randomUUID();
        ProductEntity productEntity = null;
        Unit unit = Unit.G;
        UnitValue unitValue =  null;
        float amount = 10.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Ingredient.newIngredient(product, productEntity, amount, unit, unitValue));
        assertEquals("Product cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredient2_NotSuccess() {
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId ,"Test Product");
        ProductEntity productEntity = null;
        Unit unit = null;
        UnitValue unitValue =  null;
        float amount = 10.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Ingredient.newIngredient(product, productEntity, amount, unit, unitValue));
        assertEquals("Unit cannot be null", exception.getMessage());
    }

    @Test
    void testNewIngredient3_NotSuccess() {
        Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
        UUID productEntityId = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(productEntityId,"Test Product");
        Unit unit = Unit.G;
        UnitValue unitValue = null;
        float amount = 10.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Ingredient.newIngredient(product, productEntity, amount, unit, unitValue));
        assertEquals("Only one of product or productEntity can be present", exception.getMessage());

    }

    @Test
    void testNewIngredient4_NotSuccess() {
        Product product = null;
        UUID productEntityId = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(productEntityId,"Test Product");
        Unit unit = Unit.G;
        UnitValue unitValue = UnitValue.newUnitValue(UUID.randomUUID(), productEntityId, Unit.CUP, 10.0f); ;
        float amount = 10.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Ingredient.newIngredient(product, productEntity, amount, unit, unitValue));
        assertEquals("Only one of unit or unitValue can be present", exception.getMessage());

    }

        @Test
        void testNewIngredient_InvalidAmount() {
            Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
            ProductEntity productEntity = null;
            Unit unit = Unit.KG;
            UnitValue unitValue = null;
            float amount = -5.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Ingredient.newIngredient(product, productEntity, amount, unit, unitValue));
            assertEquals("Amount must be greater than 0", exception.getMessage());
        }

        @Test
        void testNewIngredient_NullProduct() {
            ProductEntity productEntity = new ProductEntity();
            float amount = 5.0f;
            Unit unit = Unit.G;
            UnitValue unitValue = null;

            Ingredient ingredient = Ingredient.newIngredient(null, productEntity, amount, unit, unitValue);

            assertEquals(Optional.of(productEntity), ingredient.getProductEntity());
            assertEquals(Optional.empty(), ingredient.getProduct());
        }

        @Test
        void testNewIngredient_NullUnitAndUnitValue() {
            Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
            ProductEntity productEntity = null;
            float amount = 5.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Ingredient.newIngredient(product, productEntity, amount, null, null));
            assertEquals("Unit cannot be null", exception.getMessage());
        }

        @Test
        void testEqualsAndHashCode() {
            Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
            ProductEntity productEntity = null;
            float amount = 10.0f;
            Unit unit = Unit.KG;
            UnitValue unitValue = null;

            Ingredient ingredient1 = Ingredient.newIngredient(product, productEntity, amount, unit, unitValue);
            Ingredient ingredient2 = Ingredient.newIngredient(product, productEntity, amount, unit, unitValue);

            assertEquals(ingredient1, ingredient2);
            assertEquals(ingredient1.hashCode(), ingredient2.hashCode());
        }

        @Test
        void testToString() {
            Product product = Product.newProduct(UUID.randomUUID() ,"Test Product");
            ProductEntity productEntity = null;
            Unit unit = Unit.KG;
            UnitValue unitValue = null;
            float amount = 10.0f;

            Ingredient ingredient = Ingredient.newIngredient(product, productEntity, amount, unit, unitValue);

            String expected = "Ingredient{product=Product{name='test product'}, productEntity=Optional.empty, amount=10.0, unit=Optional[KG], unitValue=Optional.empty}";
            assertEquals(expected, ingredient.toString());
        }
}

