package com.ania.cookbook.domain.services;


import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
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

class RecipeServiceTest {

    private RecipeService recipeService;
    private InMemoryRecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository = Mockito.mock(InMemoryRecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);
    }


    @Test
    void testSaveRecipe() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id, "Tomato Soup", null, null, "Boil tomatoes and blend", 3);

            when(recipeRepository.findRecipeById(id)).thenReturn(Optional.empty());

            RecipeEntity savedRecipe = recipeService.saveRecipe(recipe);

            assertNotNull(savedRecipe);
            assertEquals(recipe, savedRecipe);

            verify(recipeRepository, times(1)).saveRecipe(recipe);
    }

    @Test
    void testSaveRecipeWithExistingId() {
        UUID id = UUID.randomUUID();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipeEntity = newRecipeEntity(id, "Duplicate", category, ingredients,"instruction", 9);


        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.of(recipeEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.saveRecipe(recipeEntity));

        assertEquals("A recipe with the given ID already exists.", exception.getMessage());
        verify(recipeRepository, times(1)).findRecipeById(id);
        verify(recipeRepository, never()).saveRecipe(recipeEntity);
    }
    @Test
    void testUpdateRecipe() {
        UUID id = UUID.randomUUID();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipeEntity = newRecipeEntity(id, "Updated Pancakes", category, ingredients,"instruction", 9);


        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.updateRecipe(recipeEntity)).thenReturn(recipeEntity);

        RecipeEntity updatedRecipe = recipeService.updateRecipe(recipeEntity);

        assertNotNull(updatedRecipe);
        assertEquals(recipeEntity, updatedRecipe);
        verify(recipeRepository, times(1)).findRecipeById(id);
        verify(recipeRepository, times(1)).updateRecipe(recipeEntity);
    }

    @Test
    void testUpdateNonExistingRecipe() {
        UUID id = UUID.randomUUID();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipeEntity = newRecipeEntity(id, "Pancakes", category, ingredients,"instruction", 9);

        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.updateRecipe(recipeEntity));

        assertEquals("Unable to update the recipe because it does not exist."
                , exception.getMessage());
        verify(recipeRepository, times(1)).findRecipeById(id);
        verify(recipeRepository, never()).updateRecipe(recipeEntity);
    }

    @Test
    void testDeleteRecipeById() {
        UUID id = UUID.randomUUID();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> category = List.of(Category.DESSERT);
        RecipeEntity recipeEntity = newRecipeEntity(UUID.randomUUID(), "Pancakes", category, ingredients,"instruction",9);

        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.of(recipeEntity));

        recipeService.deleteRecipeById(id);

        verify(recipeRepository, times(1)).findRecipeById(id);
        verify(recipeRepository, times(1)).deleteRecipeById(id);
    }

    @Test
    void testDeleteNonExistingRecipe() {
        UUID id = UUID.randomUUID();

        when(recipeRepository.findRecipeById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.deleteRecipeById(id));

        assertEquals("Unable to delete the recipe because it does not exist.", exception.getMessage());
        verify(recipeRepository, times(1)).findRecipeById(id);
        verify(recipeRepository, never()).deleteRecipeById(id);
    }
}