package com.ania.cookbook.infrastructure.converters;

import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.exceptions.IngredientConversionException;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class IngredientsJsonConverterTest {
    private final IngredientsJsonConverter converter = new IngredientsJsonConverter();

    @Test
    void convertToDatabaseColumn() {
        UUID productId = UUID.randomUUID();
        IngredientJson ing = new IngredientJson(productId,"Sugar", 100f, Unit.G);
        String json = converter.convertToDatabaseColumn(List.of(ing));

        assertTrue(json.contains("\"productId\":\"" + productId+"\""));
        assertTrue(json.contains("\"productName\":\"Sugar\""));
        assertTrue(json.contains("\"amount\":100"));
        assertTrue(json.contains("\"unit\":\"g\""));
    }

    @Test
    void convertToDatabaseColumnWithNull() {
        assertEquals("[]", converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToDatabaseColumnWithEmptyList() {
        assertEquals("[]", converter.convertToDatabaseColumn(Collections.emptyList()));
    }

    @Test
    void convertToDatabaseColumnShouldThrowOnBrokenObject() {
        List<IngredientJson> brokenList = List.of(new IngredientJson() {
            @Override public String getProductName() { throw new RuntimeException(); }
        });

        assertThrows(IngredientConversionException.class, () ->
                converter.convertToDatabaseColumn(brokenList)
        );
    }

    @Test
    void convertToEntityAttributeShouldParseValidJson() {
        UUID productId = UUID.randomUUID();
        String json = "[{\"productId\":\""+productId+"\",\"productName\":\"Milk\",\"amount\":200.0,\"unit\":\"ml\"}]";
        List<IngredientJson> result = converter.convertToEntityAttribute(json);

        assertEquals(1, result.size());
        assertEquals("Milk", result.getFirst().getProductName());
        assertEquals(200.0f, result.getFirst().getAmount());
        assertEquals("ml", result.getFirst().getUnit().toValue());
    }

    @Test
    void convertToEntityAttributeWithNull() {
        assertEquals(Collections.emptyList(), converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttributeWithBlank() {
        assertEquals(Collections.emptyList(), converter.convertToEntityAttribute(" "));
    }

    @Test
    void convertToEntityAttribute_shouldThrowOnInvalidJson() {
        String invalidJson = "[{ invalid }]";

        assertThrows(IngredientConversionException.class, () ->
                converter.convertToEntityAttribute(invalidJson)
        );
    }
}


