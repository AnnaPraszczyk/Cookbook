package com.ania.cookbook.domain.services;

import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReadRecipeService {
    private final InMemoryRecipeRepository recipeRepository;

    @Autowired
    public ReadRecipeService(InMemoryRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<RecipeEntity> findRecipeById(UUID id) {
        return recipeRepository.findRecipeById(id);
    }

    public Optional<RecipeEntity> findRecipeByName(String name) {
        return recipeRepository.findRecipeByName(name);
    }

    public Optional<RecipeEntity> findRecipeByCategory(String category) {
        return recipeRepository.findRecipeByCategoryContains(category);
    }

    public Optional<RecipeEntity> findRecipeByIngredient(String ingredient) {
        return recipeRepository.findRecipeByIngredientContains(ingredient);
    }

    public Optional<RecipeEntity> findRecipeByCreatedAfter(Instant created) {
        return recipeRepository.findRecipeByCreatedAfter(created);
    }
}
