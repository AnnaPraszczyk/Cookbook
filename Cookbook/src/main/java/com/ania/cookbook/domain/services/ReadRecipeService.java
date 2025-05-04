package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadRecipeService {
    private final ReadRecipe readRecipeRepository;

    public Optional<Recipe> findRecipeById(UUID id) {

        return readRecipeRepository.findRecipeById(id);
    }

    public boolean existsRecipeById(UUID id){
        return readRecipeRepository.existsRecipeById(id);
    }

    public Optional<Recipe> findRecipeByName(String name) {

        return readRecipeRepository.findRecipeByName(name);
    }

    public boolean existsRecipeByName(String name){

        return readRecipeRepository.existsRecipeByName(name);
    }

    public Optional<Recipe> findRecipeByCategory(Category category) {
        return readRecipeRepository.findRecipeByCategory(category);
    }

    public Optional<Recipe> findRecipeByIngredient(String ingredient) {
        return readRecipeRepository.findRecipeByIngredientContains(ingredient);
    }

    public Optional<Recipe> findRecipeByProductId(UUID productId){
        return readRecipeRepository.findRecipeByProductId(productId);
    }
    public Optional<Recipe> findRecipeByProductName(String productName){
        return readRecipeRepository.findRecipeByProductName(productName);
    }

    public Optional<Recipe> findRecipeByCreatedAfter(Instant created) {
        return readRecipeRepository.findRecipeByCreatedAfter(created);
    }

}
