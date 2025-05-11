package com.ania.cookbook.application.services.recipe;

import com.ania.cookbook.application.services.implementations.recipe.ReadRecipeService;
import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase.FindRecipeByName;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeRequest;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceTest {
    private RecipeService recipeService;
    private ReadRecipeService readRecipeService;
    private InMemoryRecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        readRecipeService = new ReadRecipeService(recipeRepository);
    }

    @Test
    void CreateRecipe() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe  savedRecipe = recipeService.createRecipe(new CreateRecipeRequest(
                recipe.getRecipeName(), recipe.getCategory(), recipe.getIngredients(), recipe.getInstructions(),
                recipe.getNumberOfServings(), recipe.getTags()));

        assertNotNull(savedRecipe);
        assertEquals("Pancakes", savedRecipe.getRecipeName());
    }

    @Test
    void CreateRecipeIsNull() {
        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByName(new FindRecipeByName("NonExisting")));
    }

    @Test
    void UpdateRecipe() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        recipeRepository.saveRecipe(recipe);
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(recipe.getRecipeName(),
                recipe.getCategory(), List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),
                        "Milk"), 200, Unit.G)), "Mix well", 4, List.of("Updated"));

        Recipe updatedRecipe = recipeService.updateRecipe(updateRequest);

        assertNotNull(updatedRecipe);
        assertEquals("Mix well", updatedRecipe.getInstructions());
        assertEquals(4, updatedRecipe.getNumberOfServings());
        assertEquals("Milk", updatedRecipe.getIngredients().getFirst().getProduct().getProductName());
    }

    @Test
    void UpdateRecipeWhenRecipeDoesNotExist() {
        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Unknown Recipe", Category.DESSERT, List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(request));
    }

    @Test
    void testDeleteRecipeById() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        recipeRepository.saveRecipe(recipe);
        recipeService.deleteRecipe(new DeleteRecipeRequest(recipe.getRecipeName()));

        assertTrue(recipeRepository.findRecipeById(recipe.getRecipeId()).isEmpty());


    }

    @Test
    void testDeleteRecipeByIdWhenRecipeDoesNotExist() {
        assertThrows(RecipeNotFoundException.class, () ->
                recipeService.deleteRecipe(new DeleteRecipeRequest("Unknown Recipe")));
    }
}
