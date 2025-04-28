package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Recipe;

import java.util.UUID;

public interface DeleteRecipe{
    void deleteRecipeById(UUID id);
}
