package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import java.util.List;


public interface CreateRecipeUseCase {
    Recipe createRecipe(CreateRecipe recipe);

    record CreateRecipe(
            String recipeName,
            Category category,
            List<Ingredient> ingredients,
            String instructions,
            int numberOfServings,
            List<String> tags){}

}

