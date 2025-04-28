package com.ania.cookbook.infrastructure.persistence.mapper;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.domain.model.UnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UnitValueMapperTest {
    @Test
    void testToDomainValidData() {
        UUID unitValueId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.G;
        float weight = 1.5f;

        UnitValueEntity entity = UnitValueEntity.newUnitValueEntity(unitValueId, productId, unit, weight);

        UnitValue domain = UnitValueMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(unitValueId, domain.getUnitValueId());
        assertEquals(productId, domain.getProductId());
        assertEquals(unit, domain.getUnit());
        assertEquals(weight, domain.getWeightValue());
    }

    @Test
    void testToDomainNullEntity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueMapper.toDomain(null));

        assertEquals("UnitValueEntity cannot be null", exception.getMessage());
    }

    @Test
    void testToEntityValidData() {
        UUID unitValueId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Unit unit = Unit.KG;
        float weight = 2.5f;

        UnitValue domain = UnitValue.newUnitValue(unitValueId, productId, unit, weight);

        UnitValueEntity entity = UnitValueMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(unitValueId, entity.getUnitValueId());
        assertEquals(productId, entity.getProductId());
        assertEquals(unit, entity.getUnit());
        assertEquals(weight, entity.getWeightValue());
    }

    @Test
    void testToEntityNullDomain() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UnitValueMapper.toEntity(null));

        assertEquals("UnitValue cannot be null!", exception.getMessage());
    }
}
