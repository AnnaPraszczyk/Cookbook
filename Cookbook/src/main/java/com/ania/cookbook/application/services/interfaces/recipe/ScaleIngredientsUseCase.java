package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Recipe;

public interface ScaleIngredientsUseCase {

    Recipe adjustRecipeByServings(AdjustRecipeRequest request);

    record AdjustRecipeRequest(String recipeName, int servings, int choice){}

}
