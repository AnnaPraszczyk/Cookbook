package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Unit;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class IngredientsJsonConverterTest {

    @Test
    void ListToJson() {
        Product product1 = Product.newProduct(UUID.randomUUID(), new ProductName("sugar"));
        Product product2 = Product.newProduct(UUID.randomUUID(), new ProductName("milk"));

        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(product1, 5.0f, Unit.G),
                Ingredient.newIngredient(product2, 2.0f, Unit.G)
        );

        String json = IngredientsJsonConverter.listToJson(ingredients);

        assertNotNull(json);
        assertTrue(json.contains("sugar"));
        assertTrue(json.contains("milk"));
    }

    @Test
    void ListToJsonWithEmptyList() {
        List<Ingredient> emptyList = List.of();
        String json = IngredientsJsonConverter.listToJson(emptyList);

        assertEquals("[]", json);
    }

    @Test
    void ListToJsonWithNullList() {
        String json = IngredientsJsonConverter.listToJson(null);

        assertEquals("null", json);
    }

    @Test
    void ListFromJson_WithValidJson() {
        String json = "[{\"product\":{\"productId\":\"550e8400-e29b-41d4-a716-446655440000\",\"productName\":\"Flour\"},\"amount\":10,\"unit\":\"DAG\"}]";

        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson(json);

        assertNotNull(ingredients);
        assertFalse(ingredients.isEmpty());

        assertEquals("Flour", ingredients.getFirst().getProduct().getProductName().name());
        assertEquals(10, ingredients.getFirst().getAmount());
        assertEquals("DAG", ingredients.getFirst().getUnit().toString());

    }

    @Test
    void ListFromJson_WithEmptyJson() {
        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson("[]");

        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    void ListFromJson_WithNullJson() {
        List<Ingredient> ingredients = IngredientsJsonConverter.listFromJson(null);

        assertNotNull(ingredients);
        assertTrue(ingredients.isEmpty());
    }
}


