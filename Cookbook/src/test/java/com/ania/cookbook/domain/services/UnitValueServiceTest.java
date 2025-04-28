package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryUnitValueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.UUID;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitValueServiceTest {

    @Mock
    private InMemoryUnitValueRepository unitValueRepository;
    @InjectMocks
        private UnitValueService unitValueService;


        @BeforeEach
        void setUp() {
            unitValueRepository = Mockito.mock(InMemoryUnitValueRepository.class);
            unitValueService = new UnitValueService(unitValueRepository);
        }

    @Test
    void testSaveUnitValue() {
        UUID unitValueId = UUID.randomUUID();
        UUID productEntityId = UUID.randomUUID();
        UnitValueEntity unitValueEntity = UnitValueEntity.newUnitValueEntity(unitValueId, productEntityId,Unit.CUP,150);
        String productName = "Test Product";

        unitValueService.saveUnitValue(productEntityId, unitValueEntity, productName);

        verify(unitValueRepository, times(1)).saveUnitValue(productEntityId,unitValueEntity,productName);
    }

    @Test
    void testGetUnitValuesByProductId() {
        UUID productEntityId = UUID.randomUUID();
        List<UnitValueEntity> mockList = List.of(UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), productEntityId,Unit.CUP, 2.0f));

        when(unitValueRepository.findUnitValueByProductId(productEntityId)).thenReturn(mockList);
        List<UnitValueEntity> result = unitValueService.getUnitValuesByProductId(productEntityId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(unitValueRepository, times(1)).findUnitValueByProductId(productEntityId);
    }

    @Test
    void testGetUnitValuesByProductName() {
        String productName = "Sugar";
        List<UnitValueEntity> mockList = List.of(UnitValueEntity.newUnitValueEntity(UUID.randomUUID(), UUID.randomUUID(),Unit.CAN,  0.5f));

        when(unitValueRepository.findAllUnitValuesByProductName(productName)).thenReturn(mockList);
        List<UnitValueEntity> result = unitValueService.getUnitValuesByProductName(productName);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(unitValueRepository, times(1)).findAllUnitValuesByProductName(productName);
    }

    @Test
    void testUpdateUnitValue() {
        UUID id = UUID.randomUUID();
        UUID productEntityId = UUID.randomUUID();
        Unit newUnit = Unit.JAR;
        float newWeightValue = 700.0f;

        when(unitValueRepository.updateUnitValue(productEntityId,id,newUnit,newWeightValue)).thenReturn(true);

        boolean updated = unitValueService.updateUnitValue(productEntityId,id,newUnit,newWeightValue);

        assertTrue(updated);
        verify(unitValueRepository, times(1)).updateUnitValue(productEntityId,id,newUnit,newWeightValue);
    }

    @Test
    void testDeleteUnitValue() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId = UUID.randomUUID();
        List<UnitValueEntity> mockList = List.of(UnitValueEntity.newUnitValueEntity(productId, unitValueId,Unit.CAN,  0.5f));
        when(unitValueRepository.findUnitValueByProductId(productId)).thenReturn(mockList);
        when(unitValueRepository.deleteUnitValueByProductId(productId,unitValueId)).thenReturn(true);

        boolean result = unitValueService.deleteUnitValue(productId,unitValueId);

        assertTrue(result);
        verify(unitValueRepository, times(1)).deleteUnitValueByProductId(productId,unitValueId);
    }

    @Test
    void testDeleteUnitValue_NotFound() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId = UUID.randomUUID();
        when(unitValueRepository.findUnitValueByProductId(productId)).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                unitValueService.deleteUnitValue(productId, unitValueId)
        );

        assertEquals("Unable to delete the unit because it does not exist!", exception.getMessage());
        verify(unitValueRepository, never()).deleteUnitValueByProductId(any(), any());
    }

    @Test
    void testDeleteAllUnitValuesByProductId_Success() {
        UUID productId = UUID.randomUUID();
        UUID unitValueId = UUID.randomUUID();
        List<UnitValueEntity> mockList = List.of(UnitValueEntity.newUnitValueEntity(productId, unitValueId,Unit.CAN,  0.5f));

        when(unitValueRepository.findUnitValueByProductId(productId)).thenReturn(mockList);

        unitValueService.deleteAllUnitValuesByProductId(productId);

        verify(unitValueRepository, times(1)).deleteAllUnitValuesByProductId(productId);
    }

    @Test
    void testDeleteAllUnitValuesByProductId_NotFound() {
        UUID productId = UUID.randomUUID();

        when(unitValueRepository.findUnitValueByProductId(productId)).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                unitValueService.deleteAllUnitValuesByProductId(productId)
        );

        assertEquals("Unable to delete the unit because it does not exist!", exception.getMessage());
        verify(unitValueRepository, never()).deleteAllUnitValuesByProductId(any());
    }
}