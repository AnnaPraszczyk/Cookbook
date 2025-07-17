package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientJson {
    private UUID productId;
    private String productName;
    private float amount;
    private Unit unit;
}
