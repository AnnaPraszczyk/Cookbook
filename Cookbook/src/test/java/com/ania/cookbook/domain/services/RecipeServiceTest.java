package com.ania.cookbook.domain.services;


import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {
    private RecipeService recipeService;
    private SaveRecipe saveRecipeRepository;
    private ReadRecipe readRecipeRepository;
    private UpdateRecipe updateRecipeRepository;
    private DeleteRecipe deleteRecipeRepository;

    @BeforeEach
    void setUp() {
        saveRecipeRepository = mock(SaveRecipe.class);
        readRecipeRepository = mock(ReadRecipe.class);
        updateRecipeRepository = mock(UpdateRecipe.class);
        deleteRecipeRepository = mock(DeleteRecipe.class);
        recipeService = new RecipeService(saveRecipeRepository, readRecipeRepository, updateRecipeRepository, deleteRecipeRepository);
    }

    @Test
    void testSaveRecipe() {
        Recipe recipe = mock(Recipe.class);
        UUID recipeId = UUID.randomUUID();

        when(recipe.getRecipeId()).thenReturn(recipeId);
        when(recipe.getRecipeName()).thenReturn("pancakes");
        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(false);
        when(readRecipeRepository.existsRecipeByName("pancakes")).thenReturn(false);
        when(saveRecipeRepository.saveRecipe(recipe)).thenReturn(recipe);

        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        assertNotNull(savedRecipe);
        verify(saveRecipeRepository, times(1)).saveRecipe(recipe);
    }

    @Test
    void testSaveRecipeWhenRecipeIsNull() {
        assertThrows(RecipeValidationException.class, () -> recipeService.saveRecipe(null));
    }

    @Test
    void testSaveRecipeWhenRecipeIdExists() {
        Recipe recipe = mock(Recipe.class);
        UUID recipeId = UUID.randomUUID();

        when(recipe.getRecipeId()).thenReturn(recipeId);
        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(true);

        assertThrows(RecipeValidationException.class, () -> recipeService.saveRecipe(recipe));
    }

    @Test
    void testSaveRecipeWhenRecipeNameExists() {
        Recipe recipe = mock(Recipe.class);

        when(recipe.getRecipeName()).thenReturn("pancakes");
        when(readRecipeRepository.existsRecipeByName("pancakes")).thenReturn(true);

        assertThrows(RecipeValidationException.class, () -> recipeService.saveRecipe(recipe));
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
