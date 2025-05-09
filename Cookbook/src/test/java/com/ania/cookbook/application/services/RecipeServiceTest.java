package com.ania.cookbook.application.services;


import com.ania.cookbook.TestInMemoryRecipeRepository;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecipeServiceTest {
    private final TestInMemoryRecipeRepository testInMemoryRecipeRepository = new TestInMemoryRecipeRepository();
    private final ReadRecipe readRecipeRepository = testInMemoryRecipeRepository;
    private final UpdateRecipe updateRecipeRepository = testInMemoryRecipeRepository;
    private final DeleteRecipe deleteRecipeRepository = testInMemoryRecipeRepository;
    private final SaveRecipe saveRecipeRepository = testInMemoryRecipeRepository;
    private final RecipeService recipeService = new RecipeService(saveRecipeRepository, readRecipeRepository, updateRecipeRepository, deleteRecipeRepository);

    @Test
    void testCreateRecipe() {
        Product product = Product.newProduct(UUID.randomUUID(), "Chiken breast");
        Ingredient ingredient1 = Ingredient.newIngredient(product, 2, MassUnit.DAG);
        List<Ingredient> ingredients = List.of(ingredient1);
        val recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(recipeId, "NAME", Category.APPETIZER, ingredients, "RANDOM INSTRUCTion", 2, List.of("TAG1", "TAG2"));


        recipeService.createRecipe(recipe);

        recipeService.createRecipe(null);
        val recipeById = readRecipeRepository.findRecipeById(recipeId);
        val recipe1 = recipeById.get();
        assertEquals(recipe1, recipe);
        assertEquals(recipe1.getRecipeName(), "NAME");
    }

    @Test
    void testCreateRecipeIsNull() {
        assertThrows(RecipeValidationException.class, () -> recipeService.createRecipe(null));
    }

    @Test
    void testCreateRecipeIdExists() {
        Product product = Product.newProduct(UUID.randomUUID(), "Chiken breast");
        Ingredient ingredient1 = Ingredient.newIngredient(product, 2, MassUnit.DAG);
        List<Ingredient> ingredients = List.of(ingredient1);
        val recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(recipeId, "NAME", Category.APPETIZER, ingredients, "RANDOM INSTRUCTion", 2, List.of("TAG1", "TAG2"));

        recipeService.createRecipe(recipe);

        assertThrows(RecipeValidationException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    void testCreateRecipeNameExists() {
        Recipe recipe = mock(Recipe.class);

        when(recipe.getRecipeName()).thenReturn("pancakes");
        when(readRecipeRepository.existsRecipeByName("pancakes")).thenReturn(true);

        assertThrows(RecipeValidationException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    void testUpdateRecipe() {
        Recipe recipe = mock(Recipe.class);
        UUID recipeId = UUID.randomUUID();

        when(recipe.getRecipeId()).thenReturn(recipeId);
        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(true);
        when(updateRecipeRepository.updateRecipe(recipe)).thenReturn(recipe);

        Recipe updatedRecipe = recipeService.updateRecipe(recipe);

        assertNotNull(updatedRecipe);
        verify(updateRecipeRepository, times(1)).updateRecipe(recipe);
    }

    @Test
    void testUpdateRecipeWhenRecipeDoesNotExist() {
        Recipe recipe = mock(Recipe.class);
        UUID recipeId = UUID.randomUUID();

        when(recipe.getRecipeId()).thenReturn(recipeId);
        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(recipe));
    }

    @Test
    void testDeleteRecipeById() {
        UUID recipeId = UUID.randomUUID();

        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(true);

        recipeService.deleteRecipeById(recipeId);

        verify(deleteRecipeRepository, times(1)).deleteRecipeById(recipeId);
    }

    @Test
    void testDeleteRecipeByIdWhenRecipeDoesNotExist() {
        UUID recipeId = UUID.randomUUID();

        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipeById(recipeId));
    }

    @Test
    void testToEntityValidRecipe() {
        UUID recipeId = UUID.randomUUID();
        Product product = Product.newProduct(UUID.randomUUID(), "pasta");
        Ingredient ingredient = Ingredient.newIngredient(product, 500f, MassUnit.G);
        Recipe recipe = Recipe.newRecipe(recipeId, "Spaghetti Carbonara", Category.MAIN_DISH,
                List.of(ingredient), "Cook pasta and mix with sauce", 4, List.of("Italian", "Pasta"));

        RecipeEntity recipeEntity = RecipeService.toEntity(recipe);

        assertNotNull(recipeEntity);
        assertEquals(recipe.getRecipeId(), recipeEntity.getRecipeId());
        assertEquals(recipe.getRecipeName(), recipeEntity.getRecipeName());
        assertEquals(recipe.getCategory(), recipeEntity.getCategory());
        assertEquals(recipe.getIngredients(), recipeEntity.getIngredients());
        assertEquals(recipe.getInstructions(), recipeEntity.getInstructions());
        assertEquals(recipe.getNumberOfServings(), recipeEntity.getNumberOfServings());
        assertEquals(recipe.getTags(), recipeEntity.getTags());
    }

    @Test
    void testToEntityThrowsExceptionForNullRecipe() {
        assertThrows(RecipeValidationException.class, () -> RecipeService.toEntity(null),
                "Recipe cannot be null");
    }

    @Test
    void testToDomainValidRecipeEntity() {
        UUID recipeId = UUID.randomUUID();
        Product product = Product.newProduct(UUID.randomUUID(), "pasta");
        Ingredient ingredient = Ingredient.newIngredient(product, 500f, MassUnit.G);
        List<Ingredient> ingredients = List.of(ingredient);

        RecipeEntity recipeEntity = RecipeEntity.newRecipeEntity(recipeId, "Spaghetti Carbonara", Category.MAIN_DISH, ingredients,
                "Cook pasta and mix with sauce", 4, List.of("Italian", "Pasta"));

        Recipe recipe = RecipeService.toDomain(recipeEntity);

        assertNotNull(recipe);
        assertEquals(recipeEntity.getRecipeId(), recipe.getRecipeId());
        assertEquals(recipeEntity.getRecipeName(), recipe.getRecipeName());
        assertEquals(recipeEntity.getCategory(), recipe.getCategory());
        assertEquals(recipeEntity.getIngredients(), recipe.getIngredients());
        assertEquals(recipeEntity.getInstructions(), recipe.getInstructions());
        assertEquals(recipeEntity.getNumberOfServings(), recipe.getNumberOfServings());
        assertEquals(recipeEntity.getTags(), recipe.getTags());
    }

    @Test
    void testToDomainThrowsExceptionForNullRecipeEntity() {
        assertThrows(RecipeValidationException.class, () -> RecipeService.toDomain(null),
                "RecipeEntity cannot be null");
    }


}
