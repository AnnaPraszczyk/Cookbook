package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.model.Unit;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class IngredientsJsonConverterTest {
    private final IngredientsJsonConverter converter = new IngredientsJsonConverter();
    private final ObjectMapper MAPPER = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

//    @Test
//    void ListToJson() {
//        Product product1 = Product.newProduct(UUID.randomUUID(), new ProductName("sugar"));
//        Product product2 = Product.newProduct(UUID.randomUUID(), new ProductName("milk"));
//
//        List<Ingredient> ingredients = List.of(
//                Ingredient.newIngredient(product1, 5.0f, Unit.G),
//                Ingredient.newIngredient(product2, 2.0f, Unit.G)
//        );
//
//        String json = converter.convertToDatabaseColumn(ingredients);
//
//        assertNotNull(json);
//        assertTrue(json.contains("sugar"));
//        assertTrue(json.contains("milk"));
//    }
//
//    @Test
//    void ListToJsonWithEmptyList() {
//        List<Ingredient> emptyList = List.of();
//        String json = converter.convertToDatabaseColumn(emptyList);
//
//        assertEquals("[]", json);
//    }

//    @Test
//    void listFromJsonWithValidJson() {
//        String json = "[{" +
//                "\"product\": {" +
//                "\"productId\": \"550e8400-e29b-41d4-a716-446655440000\"," +
//                "\"productName\": { \"name\": \"Flour\" }" +
//                "}," +
//                "\"amount\": 10," +
//                "\"unit\": \"DAG\"" +
//                "}]";
//
//        List<Ingredient> ingredients = converter.convertToEntityAttribute(json);
//
//        assertNotNull(ingredients);
//        assertFalse(ingredients.isEmpty());
//
//        assertEquals("Flour", ingredients.getFirst().getProduct().getProductName().name());
//        assertEquals(10, ingredients.getFirst().getAmount());
//        assertEquals("DAG", ingredients.getFirst().getUnit().toString());
//    }
//
//    @Test
//    void listFromJsonWithEmptyJson() {
//        List<Ingredient> ingredients = converter.convertToEntityAttribute("[]");
//
//        assertNotNull(ingredients);
//        assertTrue(ingredients.isEmpty());
//    }

//    @Test
//    void listFromJsonWithNullJson() {
//        List<Ingredient> ingredients = converter.convertToEntityAttribute(null);
//
//        assertNotNull(ingredients);
//        assertTrue(ingredients.isEmpty());
//    }
    @Test
    void deserializeLowercaseGToEnumG() throws Exception {
        String json = "\"g\"";
        Unit actual = MAPPER.readValue(json, Unit.class);
        assertEquals(Unit.G, actual);
    }

    @Test
    void deserializeUppercaseGToEnumG() throws Exception {
        String json = "\"G\"";
        Unit actual = MAPPER.readValue(json, Unit.class);
        assertEquals(Unit.G, actual);
    }

    @Test
    void deserializeDagToEnumDag() throws Exception {
        assertEquals(Unit.DAG, MAPPER.readValue("\"dag\"", Unit.class));
        assertEquals(Unit.DAG, MAPPER.readValue("\"DAG\"", Unit.class));
    }

//    @Test
//    void shouldConvertLowercaseUnitInIngredientJson() {
//        String json = "[{" +
//                "\"product\": {" +
//                "\"productId\": \"550e8400-e29b-41d4-a716-446655440000\"," +
//                "\"productName\": { \"name\": \"Flour\" }" +
//                "}," +
//                "\"amount\": 10," +
//                "\"unit\": \"g\"" +
//                "}]";
//
//        List<Ingredient> ingredients = converter.convertToEntityAttribute(json);
//
//        assertEquals(1, ingredients.size());
//        Ingredient ing = ingredients.getFirst();
//        assertEquals(10f, ing.getAmount());
//        assertEquals(Unit.G, ing.getUnit());
//    }
}


