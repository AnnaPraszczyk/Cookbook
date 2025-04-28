package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.UUID;

class IngredientsJsonConverterTest {

    private IngredientsJsonConverter converter;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        converter = new IngredientsJsonConverter();
    }

    @Test
    void testConvertToDatabaseColumn_withValidList() throws JsonProcessingException {
        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(id,"Salt");

        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(null,product, 10f, Unit.G, null),
                Ingredient.newIngredient(null, product,12.5f,Unit.G, null)
        );

        String json = converter.convertToDatabaseColumn(ingredients);
        assertNotNull(json);
        List<Ingredient> deserializedIngredients = objectMapper.readValue(json, new TypeReference<>() {});
        assertEquals(ingredients, deserializedIngredients);
    }

    @Test
    void testConvertToDatabaseColumn_withNullList() {
        String json = converter.convertToDatabaseColumn(null);
        assertEquals("[]", json);
    }

    @Test
    void testConvertToDatabaseColumn_withEmptyList() {
        String json = converter.convertToDatabaseColumn(List.of());
        assertEquals("[]", json);
    }

    @Test
    void testConvertToEntityAttribute_withValidJson() throws Exception {
        String json = """
                          [
                          {"productEntity":{"productId":"123e4567-e89b-12d3-a456-426614174000","productName":"Salt"},"amount":5.0},
                          {"productEntity":{"productId":"223e4567-e89b-12d3-a456-426614174000","productName":"Sugar"},"amount":10.0}
                          ]""";
        
        List<Ingredient> ingredients = converter.convertToEntityAttribute(json);

        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());

        assertEquals(5.0f, ingredients.getFirst().getAmount());
        assertNotNull(ingredients.getFirst().getProductEntity().orElse(null));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), ingredients.get(0).getProductEntity().orElseThrow().getProductId());
        assertEquals("Salt", ingredients.get(0).getProductEntity().orElseThrow().getProductName());

        assertEquals(10.0f, ingredients.get(1).getAmount());
        assertNotNull(ingredients.get(1).getProductEntity().orElse(null));
        assertEquals(UUID.fromString("223e4567-e89b-12d3-a456-426614174001"), ingredients.get(1).getProductEntity().orElseThrow().getProductId());
        assertEquals("Sugar", ingredients.get(1).getProductEntity().orElseThrow().getProductName());
    }

    @Test
    void testConvertToEntityAttribute_withEmptyJson() {
        List<Ingredient> ingredients = converter.convertToEntityAttribute("");
        assertTrue(ingredients.isEmpty());
    }

    @Test
    void testConvertToEntityAttribute_withNullJson() {
        List<Ingredient> ingredients = converter.convertToEntityAttribute(null);
        assertTrue(ingredients.isEmpty());
    }

    @Test
    void testConvertToEntityAttribute_withMalformedJson() {
        String invalidJson = "{invalid json}";
        Exception exception = assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(invalidJson));
        assertTrue(exception.getMessage().contains("Error during conversion of JSON to an ingredient list"));
    }
}