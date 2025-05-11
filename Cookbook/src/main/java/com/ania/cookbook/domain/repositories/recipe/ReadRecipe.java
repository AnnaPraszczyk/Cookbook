package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadRecipe{
    Optional<Recipe> findRecipeById(UUID id);
    boolean existsRecipeById(UUID id);
    List<Recipe> findRecipeByName(String name);
    boolean existsRecipeByName(String name);
    List<Recipe> findRecipeByCategory(Category category);
    List<Recipe> findRecipeByTag(String tag);
}
