package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UnitValueTest {

    @Test
    void testNewUnitValueValidData() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weightValue = 2.5f;

        UnitValue unitValue = UnitValue.newUnitValue(id, productId, unit, weightValue);

        assertNotNull(unitValue);
        assertEquals(id, unitValue.getUnitValueId());
        assertEquals(productId, unitValue.getProductId());
        assertEquals(unit, unitValue.getUnit());
        assertEquals(weightValue, unitValue.getWeightValue());
    }


    @Test
    void testNewUnitValueNullId() {
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;
        float weightValue = 1.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValue.newUnitValue(null, productId, unit,weightValue));
        assertEquals("UnitValueId cannot be null", exception.getMessage());
    }
    @Test
    void testNewUnitValueNullProductId() {
        UUID id = UUID.randomUUID();
        Unit unit = Unit.G;
        float weightValue = 15.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValue.newUnitValue(id, null, unit,weightValue));
        assertEquals("ProductId cannot be null", exception.getMessage());
    }

    @Test
    void testNewUnitValueNullUnit() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        float weightValue = 15.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValue.newUnitValue(id, productId, null,weightValue));
        assertEquals("Unit cannot be null", exception.getMessage());
    }
    @Test
    void testNewUnitValueNullWeightValue() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValue.newUnitValue(id, productId, unit,-5.0f));
        assertEquals("Weight value cannot be negative", exception.getMessage());
    }

    @Test
    void testConvertToGram() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        UnitValue unitValueKg = UnitValue.newUnitValue(id, productId, Unit.KG, 1.0f);
        UnitValue unitValueG = UnitValue.newUnitValue(id, productId, Unit.G, 100.0f);
        UnitValue unitValueOz = UnitValue.newUnitValue(id, productId, Unit.OZ, 10.0f);

        assertEquals(1000.0f, unitValueKg.convertToGram());
        assertEquals(100.0f, unitValueG.convertToGram());
        assertEquals(280.0f, unitValueOz.convertToGram());
    }

    @Test
    void testConvertToGramInvalidUnit() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UnitValue unitValueInvalid = UnitValue.newUnitValue(id, productId, Unit.CAN, 1.0f);

        assertThrows(IllegalArgumentException.class, unitValueInvalid::convertToGram);
    }


    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weightValue = 2.5f;

        UnitValue unitValue = UnitValue.newUnitValue(id, productId, unit, weightValue);

        String expected = "UnitValue{" +
                "unitValueId=" + id +
                ", productId=" + productId +
                ", unit=" + unit +
                ", value=" + weightValue +
                '}';
        assertEquals(expected, unitValue.toString());
    }

    @Test
    void testGetters() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weight = 2.5f;

        UnitValue unitValue = UnitValue.newUnitValue(id, productId, unit, weight);

        assertEquals(id, unitValue.getUnitValueId());
        assertEquals(productId, unitValue.getProductId());
        assertEquals(unit, unitValue.getUnit());
        assertEquals(weight, unitValue.getWeightValue());
    }
}