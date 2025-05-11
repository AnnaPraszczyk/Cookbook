package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindRecipeUseCase {

    Optional<Recipe> findRecipeById(FindRecipeById id);

    boolean existsRecipeById(FindRecipeById id);

    List<Recipe> findRecipeByName(FindRecipeByName name);

    boolean existsRecipeByName(FindRecipeByName name);

    List<Recipe> findRecipeByCategory(FindRecipeByCategory category);

    List<Recipe> findRecipeByTag(FindRecipeByTag tag);

    record FindRecipeById(UUID id){}

    record FindRecipeByName(String name){}

    record FindRecipeByCategory(Category category){}

    record FindRecipeByTag(String tag){}

}
