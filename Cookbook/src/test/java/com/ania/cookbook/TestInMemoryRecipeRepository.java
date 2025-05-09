package com.ania.cookbook;

import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TestInMemoryRecipeRepository implements SaveRecipe, ReadRecipe, UpdateRecipe, DeleteRecipe {
    private final HashMap<UUID, Recipe> inMemoryRepository = new HashMap<>();

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        inMemoryRepository.put(recipe.getRecipeId(), recipe);
        return recipe;
    }

    @Override
    public Optional<Recipe> findRecipeById(UUID id) {
        return Optional.ofNullable(inMemoryRepository.get(id));
    }

    @Override
    public boolean existsRecipeById(UUID recipeId) {
        return inMemoryRepository.containsKey(recipeId);
    }

    @Override
    public Optional<Recipe> findRecipeByName(String name) {
        return inMemoryRepository.values().stream()
                .filter(recipe -> recipe.getRecipeName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public boolean existsRecipeByName(String recipeName) {
        return inMemoryRepository.values().stream()
                .anyMatch(recipe -> recipe.getRecipeName().equalsIgnoreCase(recipeName));
    }

    @Override
    public Optional<Recipe> findRecipeByCategory(Category category) {
        return inMemoryRepository.values().stream()
                .filter(recipe -> recipe.getCategory() == category)
                .findFirst();
    }

    @Override
    public Optional<Recipe> findRecipeByIngredientContains(String ingredient) {
        return inMemoryRepository.values().stream().filter(recipe -> recipe.getIngredients()
                .stream().anyMatch(ing -> ing.getProduct().getProductName()
                        .equalsIgnoreCase(ingredient))).findFirst();
    }

    @Override
    public Optional<Recipe> findRecipeByProductId(UUID productId) {
        return inMemoryRepository.values().stream().filter(recipe -> recipe.getIngredients()
                        .stream().anyMatch(ing -> ing.getProduct().getProductId().equals(productId)))
                .findFirst();
    }

    @Override
    public Optional<Recipe> findRecipeByProductName(String productName) {
        return inMemoryRepository.values().stream().filter(recipe -> recipe.getIngredients()
                        .stream().anyMatch(ing -> ing.getProduct().getProductName().equalsIgnoreCase(productName)))
                .findFirst();
    }

    @Override
    public Optional<Recipe> findRecipeByCreatedAfter(Instant created) {
        return inMemoryRepository.values().stream()
                .filter(recipe -> recipe.getCreated().isAfter(created))
                .findFirst();
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if (!inMemoryRepository.containsKey(recipe.getRecipeId())) {
            throw new RecipeNotFoundException("Recipe with Id does not exist.");
        }
        inMemoryRepository.put(recipe.getRecipeId(), recipe);
        return recipe;
    }

    @Override
    public void deleteRecipeById(UUID id) {
        if (!inMemoryRepository.containsKey(id)) {
            throw new RecipeNotFoundException("Recipe with Id does not exist.");
        }
        inMemoryRepository.remove(id);
    }
}
