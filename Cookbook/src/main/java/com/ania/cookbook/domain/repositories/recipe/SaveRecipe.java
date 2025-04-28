package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;

public interface SaveRecipe {
    RecipeEntity saveRecipe(RecipeEntity recipe);
}
