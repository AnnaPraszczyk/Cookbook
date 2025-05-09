package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReadRecipe{
    Optional<Recipe> findRecipeById(UUID id);
    boolean existsRecipeById(UUID id);
    Optional<Recipe> findRecipeByName(String name);
    boolean existsRecipeByName(String name);
    Optional<Recipe> findRecipeByCategory(Category category);
    Optional<Recipe> findRecipeByIngredientContains(String ingredient);
    Optional<Recipe> findRecipeByProductId(UUID productId);
    Optional<Recipe> findRecipeByProductName(String productName);
    Optional<Recipe> findRecipeByCreatedAfter(Instant created);
}
