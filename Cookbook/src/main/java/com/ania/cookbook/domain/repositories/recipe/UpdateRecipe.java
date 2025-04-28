package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;

public interface UpdateRecipe {
    RecipeEntity updateRecipe(RecipeEntity recipe);
}
