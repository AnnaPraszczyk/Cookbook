package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.exceptions.RecipeValidationException;

import java.util.UUID;

import static io.micrometer.common.util.StringUtils.isBlank;

public interface DeleteRecipeUseCase {

    void deleteRecipe(DeleteRecipeCase request);
    record DeleteRecipeCase(UUID recipeId, String recipeName){
        public DeleteRecipeCase{
            if(isBlank(recipeName)){
                throw new RecipeValidationException("Recipe name cannot be null or empty.");
            }
        }
    }
}
