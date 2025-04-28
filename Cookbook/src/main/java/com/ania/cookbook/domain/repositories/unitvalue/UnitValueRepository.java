package com.ania.cookbook.domain.repositories.unitvalue;

import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.domain.model.UnitValue;
import com.ania.cookbook.infrastructure.persistence.entity.UnitValueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UnitValueRepository extends CrudRepository<UnitValue, UUID> {


        void saveUnitValue(UUID productId, UnitValueEntity unitValue, String productName);

        List<UnitValueEntity> findUnitValueByProductId(UUID productId);

        List<UnitValueEntity> findAllUnitValuesByProductName(String productName);

        boolean updateUnitValue(UUID productId, UUID unitValueId, Unit newUnit, float newWeightValue);

        boolean deleteUnitValueByProductId(UUID productId, UUID unitValueId);

        public void deleteAllUnitValuesByProductId(UUID productId);
}
