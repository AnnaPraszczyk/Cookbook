package com.ania.cookbook.application.services.interfaces.recipe;
import com.ania.cookbook.application.services.implementations.recipe.CreateRecipe;
import com.ania.cookbook.domain.model.Recipe;

public interface CreateRecipeUseCase {
    Recipe createRecipe(CreateRecipe recipe);
}

