package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import java.util.List;
import java.util.UUID;

public interface UpdateRecipeUseCase {

    Recipe updateRecipe(UUID recipeId, UpdateRecipeCase recipe);

    record UpdateRecipeCase(String name, Category category, List<Ingredient> ingredients,String instructions, int numberOfServings, List<String> tags){}
}

