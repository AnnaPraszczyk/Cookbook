package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryUnitValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UnitValueService {

    private final InMemoryUnitValueRepository repository;

    @Autowired
    public UnitValueService(InMemoryUnitValueRepository repository) {
        this.repository = repository;
    }

    public void saveUnitValue(UUID productId, UnitValueEntity unitValue, String productName) {
        repository.saveUnitValue(productId, unitValue, productName);
    }

    public List<UnitValueEntity> getUnitValuesByProductId(UUID productId) {
        return repository.findUnitValueByProductId(productId);
    }

    public List<UnitValueEntity> getUnitValuesByProductName(String productName) {
        return repository.findAllUnitValuesByProductName(productName);
    }

    public boolean updateUnitValue(UUID productId, UUID unitValueId, Unit newUnit, float newWeightValue) {
        return repository.updateUnitValue(productId, unitValueId, newUnit, newWeightValue);
    }

    public boolean deleteUnitValue(UUID productId, UUID unitValueId) {
        if (repository.findUnitValueByProductId(productId).isEmpty()) {
            throw new IllegalArgumentException("Unable to delete the unit because it does not exist!");
        }
        return repository.deleteUnitValueByProductId(productId, unitValueId);
    }

    public void deleteAllUnitValuesByProductId(UUID productId) {
        if (repository.findUnitValueByProductId(productId).isEmpty()) {
            throw new IllegalArgumentException("Unable to delete the unit because it does not exist!");
        }
        repository.deleteAllUnitValuesByProductId(productId);
    }
}

