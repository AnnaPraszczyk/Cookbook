package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static io.micrometer.common.util.StringUtils.isBlank;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadRecipeService implements FindRecipeUseCase{
    private final ReadRecipe readRecipeRepository;

    @Override
    public Optional<Recipe> findRecipeById(UUID recipeId) {
        return Optional.ofNullable(readRecipeRepository.findRecipeById(recipeId).
                orElseThrow(() -> new RecipeNotFoundException("Unable to find the recipe because it does not exist.")));
    }

    @Override
    public boolean existsRecipeById(UUID recipeId){
        if(isBlank(recipeId.toString())) {
            throw new RecipeValidationException("Recipe id cannot be null or empty.");
        }
        return readRecipeRepository.existsRecipeById(recipeId);
    }

    @Override
    public List<Recipe> findRecipeByName(String recipeName) {
        if(isBlank(recipeName)) {
            throw new RecipeValidationException("Recipe name cannot be null or empty.");
        }
        List<Recipe> matchingRecipes = readRecipeRepository.findRecipeByName(recipeName);
        if (matchingRecipes.isEmpty()) {
            throw new RecipeNotFoundException("Recipe with given name does not exist.");
        }

        return List.copyOf(matchingRecipes);
    }

    @Override
    public boolean existsRecipeByName(String recipeName){
        if(isBlank(recipeName)) {
            throw new RecipeValidationException("Recipe name cannot be null or empty.");
        }
        return readRecipeRepository.existsRecipeByName(recipeName);
    }

    @Override
    public List<Recipe> findRecipeByCategory(Category category) {
        if(category == null) {
            throw new RecipeValidationException("Recipe category cannot be null.");
        }
        List<Recipe> recipes = readRecipeRepository.findRecipeByCategory(category);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for the given category.");
        }
        return List.copyOf(recipes);
    }

    @Override
    public List<Recipe> findRecipeByTag(String tag) {
        if(isBlank(tag)) {
            throw new RecipeValidationException("Recipe tag cannot be null or empty.");
        }
        List<Recipe> recipes = readRecipeRepository.findRecipeByTag(tag);
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for the given tag.");
        }
        return List.copyOf(recipes);
    }
}
