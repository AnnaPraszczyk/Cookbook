package com.ania.cookbook.infrastructure.repositories;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUnitValueRepositoryTest {

        private InMemoryUnitValueRepository repository;

        @BeforeEach
        void setUp() {
            repository = new InMemoryUnitValueRepository();
        }

        @Test
        void testSaveUnitValue() {
            UUID id = UUID.randomUUID();
            UUID productEntityId = UUID.randomUUID();
            UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(id, productEntityId, Unit.CUP, 5.0f);
            String productName = "Example Product";

            repository.saveUnitValue(productEntityId, unitValue,productName);

            List<UnitValueEntity> unitValues = repository.findUnitValueByProductId(productEntityId);
            assertEquals(1, unitValues.size());
            assertEquals(unitValue, unitValues.getFirst());
        }

    @Test
    void testFindUnitValueByProductId() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId1 = UUID.randomUUID();
        UUID unitValueId2 = UUID.randomUUID();

        UnitValueEntity unitValue1 = UnitValueEntity.newUnitValueEntity(unitValueId1, productId, Unit.KG, 2.0f);
        UnitValueEntity unitValue2 = UnitValueEntity.newUnitValueEntity(unitValueId2, productId, Unit.DAG, 1.5f);

        repository.saveUnitValue(productId, unitValue1, "Product A");
        repository.saveUnitValue(productId, unitValue2, "Product A");

        List<UnitValueEntity> unitValues = repository.findUnitValueByProductId(productId);
        assertEquals(2, unitValues.size());
        assertTrue(unitValues.contains(unitValue1));
        assertTrue(unitValues.contains(unitValue2));
    }

    @Test
        void testFindUnitValueById_NotFound() {
            UUID productId = UUID.randomUUID();
            UUID  unitValueId = UUID.randomUUID();

            UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(unitValueId, productId,Unit.CUP, 0.0f);

            List<UnitValueEntity> result = repository.findUnitValueByProductId(productId);
            assertTrue(result.isEmpty(),"Result should be empty" );
        }

    @Test
    void testFindAllUnitValuesByProductName() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        UnitValueEntity unitValue1 = UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), productId1, Unit.KG, 2.0f);
        UnitValueEntity unitValue2 = UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), productId2, Unit.DAG, 1.5f);

        repository.saveUnitValue(productId1, unitValue1, "Sugar");
        repository.saveUnitValue(productId2, unitValue2, "Sugar");

        List<UnitValueEntity> unitValues = repository.findAllUnitValuesByProductName("Sugar");
        assertEquals(2, unitValues.size());
        assertTrue(unitValues.contains(unitValue1));
        assertTrue(unitValues.contains(unitValue2));
    }

    @Test
    void testUpdateUnitValue() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId = UUID.randomUUID();
        UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(unitValueId, productId, Unit.KG, 2.0f);

        repository.saveUnitValue(productId, unitValue, "Test Product");
        boolean updated = repository.updateUnitValue(productId, unitValueId, Unit.DAG, 25.0f);

        assertTrue(updated);
        List<UnitValueEntity> unitValues = repository.findUnitValueByProductId(productId);
        assertEquals(1, unitValues.size());
        assertEquals(Unit.DAG, unitValues.getFirst().getUnit());
        assertEquals(25.0f, unitValues.getFirst().getWeightValue());
    }

    @Test
    void testDeleteUnitValueByProductId() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId = UUID.randomUUID();
        UnitValueEntity unitValue = UnitValueEntity.newUnitValueEntity(unitValueId, productId, Unit.KG, 2.0f);

        repository.saveUnitValue(productId, unitValue, "Test Product");
        boolean deleted = repository.deleteUnitValueByProductId(productId, unitValueId);

        assertTrue(deleted);
        List<UnitValueEntity> unitValues = repository.findUnitValueByProductId(productId);
        assertTrue(unitValues.isEmpty());
    }

    @Test
    void testDeleteAllUnitValuesByProductId() {
        UUID productId = UUID.randomUUID();

        UnitValueEntity unitValue1 = UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), productId, Unit.KG, 2.0f);
        UnitValueEntity unitValue2 = UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), productId, Unit.DAG, 1.5f);

        repository.saveUnitValue(productId, unitValue1, "Test Product");
        repository.saveUnitValue(productId, unitValue2, "Test Product");

        repository.deleteAllUnitValuesByProductId(productId);

        List<UnitValueEntity> unitValues = repository.findUnitValueByProductId(productId);
        assertTrue(unitValues.isEmpty());
    }
}

