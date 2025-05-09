package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VolumeUnitTest {

    @Test
    void testDisplayName() {
        assertEquals("l", VolumeUnit.L.getDisplayName());
        assertEquals("ml", VolumeUnit.ML.getDisplayName());
        assertEquals("sp", VolumeUnit.SPOON.getDisplayName());
        assertEquals("tsp", VolumeUnit.TEASPOON.getDisplayName());
        assertEquals("gls", VolumeUnit.GLASS.getDisplayName());
        assertEquals("c", VolumeUnit.CUP.getDisplayName());
        assertEquals("pt", VolumeUnit.PT.getDisplayName());
        assertEquals("qt", VolumeUnit.QT.getDisplayName());
        assertEquals("gal", VolumeUnit.GAL.getDisplayName());
        assertEquals("pc", VolumeUnit.PIECE.getDisplayName());
        assertEquals("btl", VolumeUnit.BOTTLE.getDisplayName());
        assertEquals("cn", VolumeUnit.CAN.getDisplayName());
        assertEquals("other", VolumeUnit.OTHER.getDisplayName());
        assertEquals("pack", VolumeUnit.PACK.getDisplayName());
        assertEquals("packet", VolumeUnit.PACKET.getDisplayName());
        assertEquals("jr", VolumeUnit.JAR.getDisplayName());
        assertEquals("ctn", VolumeUnit.CARTON.getDisplayName());
    }

    @Test
    void testEnumValues() {
        VolumeUnit[] expectedValues = {
                VolumeUnit.L, VolumeUnit.ML, VolumeUnit.SPOON, VolumeUnit.TEASPOON,
                VolumeUnit.GLASS, VolumeUnit.CUP, VolumeUnit.PT, VolumeUnit.QT, VolumeUnit.GAL,
                VolumeUnit.PIECE, VolumeUnit.BOTTLE, VolumeUnit.CAN, VolumeUnit.OTHER,
                VolumeUnit.PACK, VolumeUnit.PACKET, VolumeUnit.JAR, VolumeUnit.CARTON
        };
        assertArrayEquals(expectedValues, VolumeUnit.values());
    }




}