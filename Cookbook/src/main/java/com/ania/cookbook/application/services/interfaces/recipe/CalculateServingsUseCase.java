package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Ingredient;

import java.util.List;

public interface CalculateServingsUseCase {
    int calculateServings(List<Ingredient> ingredients);
}
