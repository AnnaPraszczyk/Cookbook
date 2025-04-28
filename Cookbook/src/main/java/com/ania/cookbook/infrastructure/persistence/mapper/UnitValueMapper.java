package com.ania.cookbook.infrastructure.persistence.mapper;
import com.ania.cookbook.domain.model.UnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;


public class UnitValueMapper {

    public static UnitValue toDomain(UnitValueEntity entity) {
        if (entity == null) {throw new IllegalArgumentException("UnitValueEntity cannot be null");}
        return UnitValue.newUnitValue(
                entity.getUnitValueId(),
                entity.getProductId(),
                entity.getUnit(),
                entity.getWeightValue()
        );
    }

    public static UnitValueEntity toEntity(UnitValue domain) {
        if (domain == null) {throw new IllegalArgumentException("UnitValue cannot be null!");}
        return UnitValueEntity.newUnitValueEntity(
                domain.getUnitValueId(),
                domain.getProductId(),
                domain.getUnit(),
                domain.getWeightValue()
        );
    }
}