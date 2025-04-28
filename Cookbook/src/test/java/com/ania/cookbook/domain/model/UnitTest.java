package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void testGetDisplayName() {
        assertEquals("g", Unit.G.getDisplayName());
        assertEquals("dag", Unit.DAG.getDisplayName());
        assertEquals("kg", Unit.KG.getDisplayName());
        assertEquals("oz", Unit.OZ.getDisplayName());
        assertEquals("lbs", Unit.LBS.getDisplayName());
        assertEquals("st", Unit.STONE.getDisplayName());
        assertEquals("cwt", Unit.CWT.getDisplayName());
        assertEquals("l", Unit.L.getDisplayName());
        assertEquals("ml", Unit.ML.getDisplayName());
        assertEquals("sp", Unit.SPOON.getDisplayName());
        assertEquals("tsp", Unit.TEASPOON.getDisplayName());
        assertEquals("gls", Unit.GLASS.getDisplayName());
        assertEquals("c", Unit.CUP.getDisplayName());
        assertEquals("pt", Unit.PINT.getDisplayName());
        assertEquals("qt", Unit.QUART.getDisplayName());
        assertEquals("gal", Unit.GALLON.getDisplayName());
        assertEquals("pc", Unit.PIECE.getDisplayName());
        assertEquals("btl", Unit.BOTTLE.getDisplayName());
        assertEquals("cn", Unit.CAN.getDisplayName());
        assertEquals("pack", Unit.PACK.getDisplayName());
        assertEquals("packet", Unit.PACKET.getDisplayName());
        assertEquals("jr", Unit.JAR.getDisplayName());
        assertEquals("ctn", Unit.CARTON.getDisplayName());
    }

    @Test
    void testUnitValuesExistence() {
        Unit[] units = Unit.values();
        assertEquals(23, units.length);
    }
}
