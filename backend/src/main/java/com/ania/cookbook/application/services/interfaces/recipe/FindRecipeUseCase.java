package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindRecipeUseCase {

    Optional<Recipe> findRecipeById(UUID id);

    boolean existsRecipeById(UUID id);

    List<Recipe> findRecipeByName(String name);

    boolean existsRecipeByName(String name);

    List<Recipe> findRecipeByCategory(Category category);

    List<Recipe> findRecipeByTag(String tag);


}
