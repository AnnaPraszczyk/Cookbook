package com.ania.cookbook.infrastructure.converters;
import com.ania.cookbook.domain.exceptions.IngredientConversionException;
import com.ania.cookbook.domain.model.Ingredient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class IngredientsJsonConverter implements AttributeConverter<List<Ingredient>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Ingredient> ingredients) {
        if (ingredients == null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(ingredients);
        } catch (JsonProcessingException e) {
            throw new IngredientConversionException("Error during conversion of the ingredient list to JSON");
        }
    }

    @Override
    public List<Ingredient> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IngredientConversionException("Error during conversion of JSON to an ingredient list");
        }
    }
}
