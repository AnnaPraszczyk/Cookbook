package com.ania.cookbook.domain.repositories.unitvalue;

import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadUnitValue {

    List<UnitValueEntity> findUnitValueByProductId(UUID productId);

    List<UnitValueEntity> findAllUnitValuesByProductName(String productName);
}
