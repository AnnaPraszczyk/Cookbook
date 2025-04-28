package com.ania.cookbook.domain.repositories.unitvalue;

import com.ania.cookbook.domain.model.UnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;

import java.util.UUID;

public interface SaveUnitValue {

    void saveUnitValue(UUID productId, UnitValueEntity unitValue, String productName);
}
