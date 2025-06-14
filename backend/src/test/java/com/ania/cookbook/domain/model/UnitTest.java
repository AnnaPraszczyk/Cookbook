package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void DisplayName() {
        assertEquals("g", Unit.G.getDisplayName());
        assertEquals("dag", Unit.DAG.getDisplayName());
        assertEquals("kg", Unit.KG.getDisplayName());
        assertEquals("oz", Unit.OZ.getDisplayName());
        assertEquals("lb", Unit.LB.getDisplayName());
        assertEquals("st", Unit.ST.getDisplayName());
    }

    @Test
    void EnumValues() {
        Unit[] expectedValues = {Unit.G, Unit.DAG, Unit.KG, Unit.OZ, Unit.LB, Unit.ST};
        assertArrayEquals(expectedValues, Unit.values());
    }

    @Test
    void ConvertKilogramsToGrams() {
        float result = Unit.KG.toGrams(2); // 2 KG → G
        assertEquals(2000, result, 0.01);
    }

    @Test
    void ConvertOuncesToGrams() {
        float result = Unit.OZ.toGrams(3);
        assertEquals(84, result, 0.01);
    }

    @Test
    void ConvertPoundsToGrams() {
        float result = Unit.LB.toGrams(1); // 1 LB → G
        assertEquals(454, result, 0.01);
    }

    @Test
    void ConvertDecagramsToGrams() {
        float result = Unit.DAG.toGrams(4); // 4 DAG → G
        assertEquals(40, result, 0.01);
    }
}