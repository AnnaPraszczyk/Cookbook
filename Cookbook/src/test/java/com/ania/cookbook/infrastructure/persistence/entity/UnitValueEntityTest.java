package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.Unit;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UnitValueEntityTest {

    @Test
    void testNewUnitValueEntityValidData() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weight = 2.5f;

        UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(id, productId, unit, weight);

        assertNotNull(unitValue);
        assertEquals(id, unitValue.getUnitValueId());
        assertEquals(productId, unitValue.getProductId());
        assertEquals(unit, unitValue.getUnit());
        assertEquals(weight, unitValue.getWeightValue());
        }

    @Test
    void testNewUnitValueEntityNullId() {
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;
        float weight = 1.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueEntity.newUnitValueEntity(null, productId, unit, weight));

        assertEquals("UnitValueId cannot be null", exception.getMessage());
    }

    @Test
    void testNewUnitValueNullProductId() {
        UUID id = UUID.randomUUID();
        Unit unit = Unit.G;
        float weightValue = 15.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueEntity.newUnitValueEntity(id, null, unit,weightValue));
        assertEquals("ProductId cannot be null", exception.getMessage());
    }

    @Test
    void testNewUnitValueNullUnit() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        float weightValue = 15.0f;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueEntity.newUnitValueEntity(id, productId, null,weightValue));
        assertEquals("Unit cannot be null", exception.getMessage());
    }

    @Test
    void testNewUnitValueNullWeightValue() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueEntity.newUnitValueEntity(id, productId, unit,-5.0f));
        assertEquals("Weight value cannot be negative", exception.getMessage());
    }

    @Test
    void testConvertToGram() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        UnitValueEntity unitValueKg = UnitValueEntity.newUnitValueEntity(id, productId, Unit.KG, 1.0f);
        UnitValueEntity unitValueG = UnitValueEntity.newUnitValueEntity(id, productId, Unit.G, 100.0f);
        UnitValueEntity unitValueOz = UnitValueEntity.newUnitValueEntity(id, productId, Unit.OZ, 10.0f);

        assertEquals(1000.0f, unitValueKg.convertToGram());
        assertEquals(100.0f, unitValueG.convertToGram());
        assertEquals(280.0f, unitValueOz.convertToGram());
    }

    @Test
    void testConvertToGramInvalidUnit() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UnitValueEntity unitValueInvalid = UnitValueEntity.newUnitValueEntity(id, productId, Unit.CAN, 1.0f);

        assertThrows(IllegalArgumentException.class, unitValueInvalid::convertToGram);
    }

    @Test
    void testGetters() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weight = 2.5f;

        UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(id, productId, unit, weight);

        assertEquals(id, unitValue.getUnitValueId());
        assertEquals(productId, unitValue.getProductId());
        assertEquals(unit, unitValue.getUnit());
        assertEquals(weight, unitValue.getWeightValue());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;
        float weight = 2.0f;

        UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(id, productId, unit, weight);

        String expected = "UnitValueEntity{" +
                "unitValueId=" + id +
                ", productId=" + productId +
                ", unit=" + unit +
                ", weightValue=" + weight +
                '}';

        assertEquals(expected, unitValue.toString());
    }
}



