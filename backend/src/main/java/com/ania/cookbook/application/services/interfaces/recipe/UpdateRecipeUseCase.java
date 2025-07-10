package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.UpdateIngredientCommand;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import java.util.List;
import java.util.UUID;

public interface UpdateRecipeUseCase {

    Recipe updateRecipe(UUID recipeId, UpdateRecipeCase recipe);

    record UpdateRecipeCase(String name, Category category, List<UpdateIngredientCommand> ingredients, String instructions, int numberOfServings, List<String> tags){}
}

