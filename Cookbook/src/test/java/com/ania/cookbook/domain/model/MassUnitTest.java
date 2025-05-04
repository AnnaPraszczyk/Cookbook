package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MassUnitTest {
    @Test
    void testDisplayName() {
        assertEquals("g", MassUnit.G.getDisplayName());
        assertEquals("dag", MassUnit.DAG.getDisplayName());
        assertEquals("kg", MassUnit.KG.getDisplayName());
        assertEquals("oz", MassUnit.OZ.getDisplayName());
        assertEquals("lb", MassUnit.LB.getDisplayName());
        assertEquals("st", MassUnit.ST.getDisplayName());
    }

    @Test
    void testEnumValues() {
        MassUnit[] expectedValues = {MassUnit.G, MassUnit.DAG, MassUnit.KG, MassUnit.OZ, MassUnit.LB, MassUnit.ST};
        assertArrayEquals(expectedValues, MassUnit.values());
    }

}