package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity.newRecipeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadRecipeServiceTest {

    private ReadRecipeService readRecipeService;
    private InMemoryRecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository = Mockito.mock(InMemoryRecipeRepository.class);
        readRecipeService = new ReadRecipeService(recipeRepository);
    }

    @Test
    void testFindRecipeById() {
        UUID id = UUID.randomUUID();
        List<Ingredient> ingredient = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipe = newRecipeEntity(id, "Pancakes", category, ingredient, "None", 5);

        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.of(recipe));

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeById(id);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(recipeRepository, times(1)).findRecipeById(id);
    }

    @Test
    void testFindRecipeByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.empty());

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeById(id);

        assertFalse(foundRecipe.isPresent());
        verify(recipeRepository, times(1)).findRecipeById(id);
    }

    @Test
    void testFindRecipeByName() {
        String name = "Pancakes";
        List<Ingredient> ingredient = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipe = newRecipeEntity(UUID.randomUUID(),name, category, ingredient,"instructions" , 7);

        when(recipeRepository.findRecipeByName(name)).thenReturn(Optional.of(recipe));

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByName(name);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(recipeRepository, times(1)).findRecipeByName(name);
    }

    @Test
    void testFindRecipeByNameNotFound() {
        String name = "Nonexistent Recipe";

        when(recipeRepository.findRecipeByName(name)).thenReturn(Optional.empty());

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByName(name);

        assertFalse(foundRecipe.isPresent());
        verify(recipeRepository, times(1)).findRecipeByName(name);
    }

    @Test
    void testFindRecipeByCategory() {
        List<Ingredient> ingredient = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipe = newRecipeEntity(UUID.randomUUID(), "Pancakes", category, ingredient,"instructions",  3);

        when(recipeRepository.findRecipeByCategoryContains(category.toString())).thenReturn(Optional.of(recipe));

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByCategory(category.toString());

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(recipeRepository, times(1)).findRecipeByCategoryContains(category.toString());
    }
    @Test
    void testFindRecipeByCategoryNotFound() {
        String category = "Nonexistent Category";

        when(recipeRepository.findRecipeByCategoryContains(category)).thenReturn(Optional.empty());

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByCategory(category);

        assertFalse(foundRecipe.isPresent());
        verify(recipeRepository, times(1)).findRecipeByCategoryContains(category);
    }

    @Test
    void testFindRecipeByIngredient() {
        ProductEntity productEntity = ProductEntity.newProductEntity(UUID.randomUUID(), "Test Product");
        Ingredient ingredient = Ingredient.newIngredient(null, productEntity,5f, Unit.DAG,null);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipe = newRecipeEntity(UUID.randomUUID(), "Pancakes", category, ingredients,"instruction", 9);

        when(recipeRepository.findRecipeByIngredientContains(ingredient.toString())).thenReturn(Optional.of(recipe));

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByIngredient(ingredient.toString());

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(recipeRepository, times(1)).findRecipeByIngredientContains(ingredient.toString());
    }

    @Test
    void testFindRecipeByIngredientNotFound() {
        String ingredient = "Nonexistent Ingredient";

        when(recipeRepository.findRecipeByIngredientContains(ingredient)).thenReturn(Optional.empty());

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByIngredient(ingredient);

        assertFalse(foundRecipe.isPresent());
        verify(recipeRepository, times(1)).findRecipeByIngredientContains(ingredient);
    }

    @Test
    void testFindRecipeByCreatedAfter() {
        Instant date = Instant.now().minusSeconds(3600);
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipe = newRecipeEntity(UUID.randomUUID(), "Pancakes", category, ingredients,"instruction", 9);

        when(recipeRepository.findRecipeByCreatedAfter(date)).thenReturn(Optional.of(recipe));

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByCreatedAfter(date);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(recipeRepository, times(1)).findRecipeByCreatedAfter(date);
    }
    @Test
    void testFindRecipeByCreatedAfterNotFound() {
        Instant date = Instant.now();

        when(recipeRepository.findRecipeByCreatedAfter(date)).thenReturn(Optional.empty());

        Optional<RecipeEntity> foundRecipe = readRecipeService.findRecipeByCreatedAfter(date);

        assertFalse(foundRecipe.isPresent());
        verify(recipeRepository, times(1)).findRecipeByCreatedAfter(date);
    }
}