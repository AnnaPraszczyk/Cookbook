package com.ania.cookbook.infrastructure.converters;
import com.ania.cookbook.domain.exceptions.IngredientConversionException;
import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;

@Converter(autoApply = false)
public class IngredientsJsonConverter implements AttributeConverter<List<IngredientJson>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<IngredientJson> ingredients) {
        if (ingredients == null || ingredients.isEmpty()){
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(ingredients);
        } catch (JsonProcessingException e) {
            throw new IngredientConversionException("Error during conversion of the ingredient list to JSON");
        }
    }

    @Override
    public List<IngredientJson> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<IngredientJson>>() {
            });
        } catch (Exception e) {
            throw new IngredientConversionException("Error during conversion of JSON to an ingredient list");
        }
    }
}
