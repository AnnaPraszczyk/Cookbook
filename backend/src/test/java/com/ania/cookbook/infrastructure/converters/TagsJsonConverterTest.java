package com.ania.cookbook.infrastructure.converters;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TagsJsonConverterTest {
    private final TagsJsonConverter converter = new TagsJsonConverter();

    @Test
    void convertToDatabaseColumnWithTags() {
        List<String> tags = List.of("vegan", "gluten-free");
        String json = converter.convertToDatabaseColumn(tags);

        assertEquals("[\"vegan\",\"gluten-free\"]", json);
    }

    @Test
    void convertToDatabaseColumnWithNull() {
        String json = converter.convertToDatabaseColumn(null);
        assertEquals("[]", json);
    }

    @Test
    void convertToDatabaseColumnWithEmptyList() {
        String json = converter.convertToDatabaseColumn(new ArrayList<>());
        assertEquals("[]", json);
    }

    @Test
    void convertToEntityAttributeWithValidJson() {
        String json = "[\"spicy\",\"dinner\"]";
        List<String> tags = converter.convertToEntityAttribute(json);

        assertEquals(2, tags.size());
        assertTrue(tags.contains("spicy"));
        assertTrue(tags.contains("dinner"));
    }

    @Test
    void convertToEntityAttributeWithNull() {
        List<String> result = converter.convertToEntityAttribute(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttributeWithInvalidJson() {
        String brokenJson = "[{ invalid json }]";

        assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute(brokenJson)
        );
    }

    @Test
    void convertToEntityAttributeWithEmptyJson() {
        String json = "";
        List<String> result = converter.convertToEntityAttribute(json);
        assertTrue(result.isEmpty());
    }
}