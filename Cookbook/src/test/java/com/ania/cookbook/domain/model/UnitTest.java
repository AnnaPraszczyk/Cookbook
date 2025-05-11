package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {
    @Test
    void testDisplayName() {
        assertEquals("g", Unit.G.getDisplayName());
        assertEquals("dag", Unit.DAG.getDisplayName());
        assertEquals("kg", Unit.KG.getDisplayName());
        assertEquals("oz", Unit.OZ.getDisplayName());
        assertEquals("lb", Unit.LB.getDisplayName());
        assertEquals("st", Unit.ST.getDisplayName());
    }

    @Test
    void testEnumValues() {
        Unit[] expectedValues = {Unit.G, Unit.DAG, Unit.KG, Unit.OZ, Unit.LB, Unit.ST};
        assertArrayEquals(expectedValues, Unit.values());
    }

}