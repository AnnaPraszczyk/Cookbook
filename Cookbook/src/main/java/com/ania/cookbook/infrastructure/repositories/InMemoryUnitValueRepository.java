package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.domain.repositories.unitvalue.DeleteUnitValue;
import com.ania.cookbook.domain.repositories.unitvalue.ReadUnitValue;
import com.ania.cookbook.domain.repositories.unitvalue.SaveUnitValue;
import com.ania.cookbook.domain.repositories.unitvalue.UpdateUnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUnitValueRepository implements SaveUnitValue, ReadUnitValue, UpdateUnitValue, DeleteUnitValue {

    private final HashMap<UUID, List<UnitValueEntity>> inMemoryRepository = new HashMap<>();
    private final HashMap<UUID, String> inMemoryRepositoryByName = new HashMap<>();

    @Override
    public void saveUnitValue(UUID productId, UnitValueEntity unitValue, String productName) {
        inMemoryRepository.computeIfAbsent(productId, k->new ArrayList<>()).add(unitValue);
        inMemoryRepositoryByName.put(productId, productName);
    }

    @Override
    public List<UnitValueEntity> findUnitValueByProductId(UUID productId){
        return inMemoryRepository.getOrDefault(productId, Collections.emptyList());
    }
    @Override
    public List<UnitValueEntity> findAllUnitValuesByProductName(String productName){
        List<UUID> matchingProductsIds = inMemoryRepositoryByName.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(productName))
                .map(Map.Entry::getKey)
                .toList();
        return matchingProductsIds.stream().flatMap(productId -> inMemoryRepository
                .getOrDefault(productId, Collections.emptyList()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateUnitValue(UUID productId, UUID unitValueId, Unit newUnit, float newWeightValue) {
        List<UnitValueEntity> unitValues = inMemoryRepository.get(productId);

        if (unitValues != null) {
            for (int i = 0; i < unitValues.size(); i++) {
                UnitValueEntity current = unitValues.get(i);
                if (current.getUnitValueId().equals(unitValueId)) {
                    UnitValueEntity updated = UnitValueEntity.newUnitValueEntity(
                            current.getUnitValueId(),
                            current.getProductId(),
                            newUnit,
                            newWeightValue);
                    unitValues.set(i, updated);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteUnitValueByProductId(UUID productId, UUID unitValueId){
        List<UnitValueEntity> unitValues = inMemoryRepository.get(productId);
        if(unitValues != null){
            return unitValues.removeIf(unitValue -> unitValue.getUnitValueId().equals(unitValueId));
        }
        return false;
    }

    @Override
    public void deleteAllUnitValuesByProductId(UUID productId){
        inMemoryRepository.remove(productId);
    }

}
