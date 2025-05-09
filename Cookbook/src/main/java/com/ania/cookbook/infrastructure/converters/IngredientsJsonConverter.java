package com.ania.cookbook.infrastructure.converters;
import com.ania.cookbook.domain.model.Ingredient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class IngredientsJsonConverter{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String listToJson(List<Ingredient> ingredients) {
        if (ingredients == null){
            return "null";
        }
        if(ingredients.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(ingredients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during conversion of the ingredient list to JSON", e);
        }
    }


    public static List<Ingredient> listFromJson(String json) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Ingredient.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during conversion of JSON to an ingredient list", e);
        }
    }
}
