package com.ania.cookbook.infrastructure.converters;
import com.ania.cookbook.domain.model.Ingredient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class IngredientsJsonConverter implements AttributeConverter<List<Ingredient>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Ingredient> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error during conversion of the ingredient list to JSON", e);
        }
    }

    @Override
    public List<Ingredient> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Ingredient>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error during conversion of JSON to an ingredient list", e);
        }
    }
}
