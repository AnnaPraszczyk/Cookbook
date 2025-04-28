package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReadRecipe {
    Optional<RecipeEntity> findRecipeById(UUID id);
    Optional<RecipeEntity> findRecipeByName(String name);
    Optional<RecipeEntity> findRecipeByCategoryContains(String category);
    Optional<RecipeEntity> findRecipeByIngredientContains(String ingredient);
    Optional<RecipeEntity> findRecipeByProduct(ProductEntity product);
    Optional<RecipeEntity> findRecipeByCreatedAfter(Instant created);
}
