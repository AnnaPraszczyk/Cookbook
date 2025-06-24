package com.ania.cookbook.infrastructure.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class TagsJsonConverter implements AttributeConverter<List<String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> tags) {
        try {
            return objectMapper.writeValueAsString(tags != null ? tags : new ArrayList<>());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting tags to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return dbData != null
                    ? objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class))
                    : new ArrayList<>();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading tags from JSON", e);
        }
    }



}
