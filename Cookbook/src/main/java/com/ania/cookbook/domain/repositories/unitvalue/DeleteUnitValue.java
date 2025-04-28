package com.ania.cookbook.domain.repositories.unitvalue;

import com.ania.cookbook.domain.model.UnitValue;

import java.util.UUID;

public interface DeleteUnitValue {

    boolean deleteUnitValueByProductId(UUID productId, UUID unitValueId);

    void deleteAllUnitValuesByProductId(UUID productId);
}
