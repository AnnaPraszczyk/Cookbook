package com.ania.cookbook.domain.repositories.unitvalue;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.domain.model.UnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;

import java.util.UUID;

public interface UpdateUnitValue {

    boolean updateUnitValue(UUID productId, UUID unitValueId, Unit newUnit, float newWeightValue);
}
