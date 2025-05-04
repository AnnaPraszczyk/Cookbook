package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.MassUnit;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.VolumeUnit;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class IngredientsJsonConverterTest {

    @Test
    void testListToJson() {
        Product product1 = Product.newProduct(UUID.randomUUID(), "sugar");
        Product product2 = Product.newProduct(UUID.randomUUID(), "milk");

        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(product1, 5.0f, MassUnit.G),
                Ingredient.newIngredient(product2, 2.0f, VolumeUnit.CUP, 250.0f, MassUnit.G)
        );

        String json = IngredientsJsonConverter.listToJson(ingredients);

        assertNotNull(json);
        assertTrue(json.contains("sugar"));
        assertTrue(json.contains("milk"));
    }

    @Test
    void testListToJsonWithEmptyList() {
        List<Ingredient> emptyList = List.of();
        String json = IngredientsJsonConverter.listToJson(emptyList);

        assertEquals("[]", json);
    }

    @Test
    void testListToJsonWithNullList() {
        String json = IngredientsJsonConverter.listToJson(null);

        assertEquals("null", json);
    }

    @Test
    void testListFromJson_WithValidJson() {
        String json = "[{\"product\":{\"productId\":\"123e4567-e89b-12d3-a456-426614174000\",\"productName\":\"Sugar\"},\"amount\":5.0,\"massUnit\":\"G\"}]";

        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson(json);

        assertNotNull(ingredients);
        assertEquals(1, ingredients.size());
        assertEquals("Sugar", ingredients.getFirst().getProduct().getProductName());
    }

    @Test
    void testListFromJson_WithEmptyJson() {
        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson("[]");

        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    void testListFromJson_WithNullJson() {
        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson(null);

        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }
}


