package com.ania.cookbook.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void CategoryValuesExistence() {
        Category[] categories = Category.values();
        assertEquals(12, categories.length);
    }

    @Test
    void SerializeCategoryToJson() throws Exception {
        Category category = Category.MAIN_COURSE;
        String json = objectMapper.writeValueAsString(category);

        assertEquals("\"Main Course\"", json);
    }

    @Test
    void shouldDeserializeJsonToCategory() throws Exception {
        String json = "\"Main Course\"";
        Category category = objectMapper.readValue(json, Category.class);

        assertEquals(Category.MAIN_COURSE, category);
    }
}




