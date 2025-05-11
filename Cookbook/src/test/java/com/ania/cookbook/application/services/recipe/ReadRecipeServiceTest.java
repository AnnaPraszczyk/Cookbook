package com.ania.cookbook.application.services.recipe;

import com.ania.cookbook.application.services.implementations.recipe.ReadRecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase.FindRecipeByCategory;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase.FindRecipeByTag;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase.FindRecipeByName;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase.FindRecipeById;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ReadRecipeServiceTest {

    private ReadRecipeService readRecipeService;
    private InMemoryRecipeRepository readRecipeRepository;


    @BeforeEach
    void setUp() {
        readRecipeRepository = new InMemoryRecipeRepository();
        readRecipeService = new ReadRecipeService(readRecipeRepository);
    }

    @Test
    void FindRecipeById() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeById(new FindRecipeById(recipe.getRecipeId()));

        assertTrue(foundRecipe.isPresent());
        assertEquals("Pancakes", foundRecipe.get().getRecipeName());
    }

    @Test
    void FindRecipeByIdNotFound() {
        RecipeNotFoundException exception = assertThrows(RecipeNotFoundException.class,
                () -> readRecipeService.findRecipeById(new FindRecipeById(UUID.randomUUID())));
        assertEquals("Unable to find the recipe because it does not exist.", exception.getMessage());
    }

    @Test
    void ExistsRecipeById() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        assertTrue(readRecipeService.existsRecipeById(new FindRecipeById(recipe.getRecipeId())));
    }

    @Test
    void ExistsRecipeByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
         assertFalse(readRecipeService.existsRecipeById(new FindRecipeById(nonExistentId)));
    }

    @Test
    void testFindRecipeByName() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10,Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByName(new FindRecipeByName("Pancakes"));

        assertFalse(foundRecipes.isEmpty());
        assertEquals("Pancakes", foundRecipes.getFirst().getRecipeName());

    }

    @Test
    void testFindRecipeByNameNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByName(new FindRecipeByName("NonExisting")));
    }
    @Test
    void testExistsRecipeByName() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10,Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        assertTrue(readRecipeService.existsRecipeByName(new FindRecipeByName(recipe.getRecipeName())));
    }

    @Test
    void testExistsRecipeByNameNotFound() {
        String nonExistentRecipeName = "Nonexistent Recipe";
        assertFalse(readRecipeService.existsRecipeByName(new FindRecipeByName(nonExistentRecipeName)));
    }

    @Test
    void FindRecipeByCategory() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10,Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByCategory(new FindRecipeByCategory(Category.DESSERT));

        assertFalse(foundRecipes.isEmpty());
        assertEquals(Category.DESSERT, foundRecipes.getFirst().getCategory());

    }

    @Test
    void FindRecipeByTag() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),10,Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        readRecipeRepository.saveRecipe(recipe);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByTag(new FindRecipeByTag("Easy"));

        assertFalse(foundRecipes.isEmpty());
        assertTrue(foundRecipes.getFirst().getTags().contains("Easy"));
    }

    @Test
    void RecipeNotFoundByTag() {
        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByTag(new FindRecipeByTag("NonExistingTag")));
    }
}