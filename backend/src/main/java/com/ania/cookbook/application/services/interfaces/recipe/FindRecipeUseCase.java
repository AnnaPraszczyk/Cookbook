package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindRecipeUseCase {

    Optional<Recipe> findRecipeById(UUID id);

    boolean existsRecipeById(UUID id);

    List<Recipe> findRecipeByName(String name);
    Page<Recipe> findRecipeByName(String name, Pageable pageable);

    boolean existsRecipeByName(String name);

    List<Recipe> findRecipeByCategory(Category category);
    Page<Recipe> findRecipeByCategory(Category category, Pageable pageable);



}
