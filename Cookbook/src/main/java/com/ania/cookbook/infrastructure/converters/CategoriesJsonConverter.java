package com.ania.cookbook.infrastructure.converters;
import com.ania.cookbook.domain.model.Category;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
@Converter
public class CategoriesJsonConverter implements AttributeConverter<List<Category>, String> {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<Category> attribute) {
            if (attribute == null) {
                throw new RuntimeException("Error during conversion of the category list to JSON");
            }
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (Exception e) {
                throw new RuntimeException("Error during conversion of the category list to JSON", e);
            }
        }

        @Override
        public List<Category> convertToEntityAttribute(String dbData) {
            try {
                return objectMapper.readValue(dbData, new TypeReference<List<Category>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Error during conversion of JSON to a category list", e);
            }
        }
}

