package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
@Component
public class InMemoryRecipeRepository implements SaveRecipe, ReadRecipe, UpdateRecipe, DeleteRecipe {
    private final HashMap<UUID, RecipeEntity> inMemoryRepository = new HashMap<>();
    private final HashMap<String, RecipeEntity> inMemoryRepositoryByName = new HashMap<>();
    @Override
    public RecipeEntity saveRecipe(RecipeEntity recipe){
        inMemoryRepository.put(recipe.getRecipeId(), recipe);
        inMemoryRepositoryByName.put(recipe.getRecipeName(), recipe);
        return recipe;
    }
    @Override
    public Optional<RecipeEntity> findRecipeById(UUID id){
        return Optional.ofNullable(inMemoryRepository.get(id));
    }
    @Override
    public Optional<RecipeEntity> findRecipeByName(String name){
        return Optional.ofNullable(inMemoryRepositoryByName.get(name));
    }
    @Override
    public Optional<RecipeEntity> findRecipeByCategoryContains(String category){
        return inMemoryRepository.values().stream().filter(recipe -> recipe.getCategories().stream()
                .anyMatch(cat ->cat.toString().equalsIgnoreCase(category))).findFirst();
    }
    @Override
    public Optional<RecipeEntity> findRecipeByIngredientContains(String ingredient){
        return inMemoryRepository.values().stream().filter(recipe -> recipe.getIngredients()
                .stream().anyMatch(ing -> ing.getProductEntity().map(ProductEntity::getProductName)
                        .orElse("").equalsIgnoreCase(ingredient))).findFirst();
    }
    @Override
    public Optional<RecipeEntity> findRecipeByProduct(ProductEntity product){
        return Optional.ofNullable(inMemoryRepository.get(product.getProductId()));
    }
    @Override
    public Optional<RecipeEntity> findRecipeByCreatedAfter(Instant created){
        return inMemoryRepository.values().stream()
                .filter(recipe ->recipe.getCreated().isAfter(created))
                .findFirst();
    }
    @Override
    public RecipeEntity updateRecipe(RecipeEntity recipe){
        inMemoryRepository.put(recipe.getRecipeId(), recipe);
        return recipe;
    }
    @Override
    public void deleteRecipeById(UUID id){
        inMemoryRepository.remove(id);
    }
}
