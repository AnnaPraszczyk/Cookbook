package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void displayName() {
        assertEquals("g", Unit.G.getDisplayName());
        assertEquals("dag", Unit.DAG.getDisplayName());
        assertEquals("kg", Unit.KG.getDisplayName());
        assertEquals("oz", Unit.OZ.getDisplayName());
        assertEquals("lb", Unit.LB.getDisplayName());
        assertEquals("st", Unit.ST.getDisplayName());
        assertEquals("ml", Unit.ML.getDisplayName());
        assertEquals("cl", Unit.CL.getDisplayName());
        assertEquals("dl", Unit.DL.getDisplayName());
        assertEquals("l", Unit.L.getDisplayName());
        assertEquals("tsp", Unit.TSP.getDisplayName());
        assertEquals("tbsp", Unit.TBSP.getDisplayName());
        assertEquals("cup", Unit.CUP.getDisplayName());
        assertEquals("pt", Unit.PT.getDisplayName());
        assertEquals("qt", Unit.QT.getDisplayName());
        assertEquals("gal", Unit.GAL.getDisplayName());
        assertEquals("pc", Unit.PC.getDisplayName());
        assertEquals("sl", Unit.SL.getDisplayName());
        assertEquals("pn", Unit.PN.getDisplayName());
    }

    @Test
    void enumValues() {
        Unit[] expectedValues = {Unit.G, Unit.DAG, Unit.KG, Unit.OZ, Unit.LB, Unit.ST,
                Unit.ML, Unit.CL, Unit.DL, Unit.L, Unit.TSP, Unit.TBSP,
                Unit.CUP, Unit.PT, Unit.QT, Unit.GAL, Unit.PC, Unit.SL, Unit.PN};
        assertArrayEquals(expectedValues, Unit.values());
    }

    @Test
    void convertKilogramsToGrams() {
        float result = Unit.KG.toGrams(2);
        assertEquals(2000, result, 0.01);
    }

    @Test
    void convertOuncesToGrams() {
        float result = Unit.OZ.toGrams(3);
        assertEquals(84, result, 0.01);
    }

    @Test
    void convertPoundsToGrams() {
        float result = Unit.LB.toGrams(1);
        assertEquals(454, result, 0.01);
    }

    @Test
    void convertDecagramsToGrams() {
        float result = Unit.DAG.toGrams(4);
        assertEquals(40, result, 0.01);
    }
}