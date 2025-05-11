package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Repository
public class InMemoryRecipeRepository implements SaveRecipe, ReadRecipe, UpdateRecipe, DeleteRecipe {
    private final HashMap<UUID, Recipe> recipes = new HashMap<>();

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        recipes.put(recipe.getRecipeId(), recipe);
        return recipe;
    }

    @Override
    public Optional<Recipe> findRecipeById(UUID recipeId) {
        return Optional.ofNullable(recipes.get(recipeId));
    }

    @Override
    public boolean existsRecipeById(UUID recipeId) {
        return recipes.containsKey(recipeId);
    }

    @Override
    public List<Recipe> findRecipeByName(String name) {
        return recipes.values().stream()
                .filter(recipe -> recipe.getRecipeName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public boolean existsRecipeByName(String name) {
        return recipes.values().stream()
                .anyMatch(recipe -> recipe.getRecipeName().equalsIgnoreCase(name));
    }

    @Override
    public List<Recipe> findRecipeByCategory(Category category){
        return recipes.values().stream()
                .filter(recipe -> recipe.getCategory().equals(category))
                .toList();
    }

    @Override
    public List<Recipe> findRecipeByTag(String tag){
        return recipes.values().stream()
                .filter(recipe -> recipe.getTags().contains(tag))
                .toList();
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if (!recipes.containsKey(recipe.getRecipeId())) {
            throw new RecipeNotFoundException("Recipe not found!");
        }
        recipes.put(recipe.getRecipeId(), recipe);
        return recipe;
    }

    @Override
    public void deleteRecipeById(UUID recipeId) {
        recipes.remove(recipeId);
    }
}

