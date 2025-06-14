package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Recipe;

import java.util.UUID;

public interface ScaleIngredientsUseCase {

    Recipe adjustRecipeByServings(AdjustRecipe recipe);

    record AdjustRecipe(UUID recipeId, int servings){
        public AdjustRecipe{
            if(recipeId == null){
                throw new RecipeNotFoundException("Recipe with given Id does not exist.");
            }
            if(servings<=0) {
                throw new RecipeValidationException("Number of servings must be greater than zero.");
            }
        }

    }

}
