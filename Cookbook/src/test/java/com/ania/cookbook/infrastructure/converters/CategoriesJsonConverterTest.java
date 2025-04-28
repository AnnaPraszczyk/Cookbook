package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.model.Category;
import org.junit.jupiter.api.Test;
import java.util.List;

class CategoriesJsonConverterTest {

    private final CategoriesJsonConverter converter = new CategoriesJsonConverter();
    @Test
    void testConvertToDatabaseColumn_ShouldReturnJson_WhenValidListProvided() {
        List<Category> categories = List.of(Category.DESSERT, Category.VEGAN);

        String jsonResult = converter.convertToDatabaseColumn(categories);

        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("DESSERT"));
        assertTrue(jsonResult.contains("VEGAN"));
    }

    @Test
    void testConvertToDatabaseColumn_ShouldThrowException_WhenNullProvided() {
        Exception exception = assertThrows(RuntimeException.class, () -> converter.convertToDatabaseColumn(null));
        assertEquals("Error during conversion of the category list to JSON", exception.getMessage());
    }

    @Test
    void testConvertToEntityAttribute_ShouldReturnList_WhenValidJsonProvided() {
        String json = "[\"DESSERT\",\"VEGAN\"]";

        List<Category> categories = converter.convertToEntityAttribute(json);

        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.contains(Category.DESSERT));
        assertTrue(categories.contains(Category.VEGAN));
    }
    @Test
    void testConvertToEntityAttribute_ShouldThrowException_WhenNullProvided() {
        Exception exception = assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(null));
        assertEquals("Error during conversion of JSON to a category list", exception.getMessage());
    }

    @Test
    void testConvertToEntityAttribute_ShouldThrowException_WhenInvalidJsonProvided() {
        String invalidJson = "{invalid}";

        Exception exception = assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(invalidJson));
        assertEquals("Error during conversion of JSON to a category list", exception.getMessage());
    }
}