package com.ania.cookbook.application.services.interfaces.recipe;
import com.ania.cookbook.application.services.implementations.recipe.RecipeCommand;
import com.ania.cookbook.domain.model.Recipe;

public interface CreateRecipeUseCase {
    Recipe createRecipe(RecipeCommand recipe);
}

