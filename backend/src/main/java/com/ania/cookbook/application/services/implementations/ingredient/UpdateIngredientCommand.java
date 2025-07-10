package com.ania.cookbook.application.services.implementations.ingredient;

import com.ania.cookbook.domain.model.Unit;

public record UpdateIngredientCommand(String productName, float amount, Unit unit) {
}
