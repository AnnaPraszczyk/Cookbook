package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReadRecipeServiceTest {

    private ReadRecipeService readRecipeService;
    private IngredientService ingredientService;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        readRecipeService = new ReadRecipeService(recipeRepository);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        ingredientService = new IngredientService(productService);
    }

    @Test
    void findRecipeById() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeById(savedRecipe.getRecipeId());

        assertTrue(foundRecipe.isPresent());
        assertEquals(savedRecipe.getRecipeName(), foundRecipe.get().getRecipeName());
    }

    @Test
    void findRecipeByIdNotFound() {
        RecipeNotFoundException exception = assertThrows(RecipeNotFoundException.class,
                () -> readRecipeService.findRecipeById(UUID.randomUUID()));
        assertEquals("Unable to find the recipe because it does not exist.", exception.getMessage());
    }

    @Test
    void existsRecipeById() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);

        assertTrue(readRecipeService.existsRecipeById(savedRecipe.getRecipeId()));
    }

    @Test
    void existsRecipeByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
         assertFalse(readRecipeService.existsRecipeById(nonExistentId));
    }

    @Test
    void findRecipeByName() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));
        Recipe savedRecipe = recipeService.createRecipe(request);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByName(savedRecipe.getRecipeName());

        assertFalse(foundRecipes.isEmpty());
        assertEquals(savedRecipe.getRecipeName(), foundRecipes.getFirst().getRecipeName());

    }

    @Test
    void findRecipeByNameNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByName("NonExisting"));
    }

    @Test
    void existsRecipeByName() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);

        assertTrue(readRecipeService.existsRecipeByName(savedRecipe.getRecipeName()));
    }

    @Test
    void existsRecipeByNameNotFound() {
        String nonExistentRecipeName = "Nonexistent Recipe";
        assertFalse(readRecipeService.existsRecipeByName(nonExistentRecipeName));
    }

    @Test
    void findRecipeByCategory() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByCategory(savedRecipe.getCategory());

        assertFalse(foundRecipes.isEmpty());
        assertEquals(savedRecipe.getCategory(), foundRecipes.getFirst().getCategory());

    }

    @Test
    void findRecipeByTag() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);
        List<Recipe> foundRecipes = readRecipeService.findRecipeByTag(savedRecipe.getTags().getFirst());

        assertFalse(foundRecipes.isEmpty());
        assertTrue(foundRecipes.getFirst().getTags().contains("Easy"));
    }

    @Test
    void recipeNotFoundByTag() {
        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByTag("NonExistingTag"));
    }
}