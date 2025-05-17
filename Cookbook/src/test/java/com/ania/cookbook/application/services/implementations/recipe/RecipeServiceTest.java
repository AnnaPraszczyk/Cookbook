package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeRequest;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    private InMemoryRecipeRepository recipeRepository;
    private RecipeService recipeService;
    private ReadRecipeService readRecipeService;
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        readRecipeService = new ReadRecipeService(recipeRepository);
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        ingredientService = new IngredientService(productService);
    }

    @Test
    void createRecipe() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName("Flour"),20,Unit.DAG);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Sugar"),10,Unit.G);
        List<Ingredient> ingredients = List.of(ingredient1,ingredient2);

        CreateRecipeRequest request = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                ingredients, "Mix and fry", 2, List.of("Easy"));

        Recipe savedRecipe = recipeService.createRecipe(request);

        assertNotNull(savedRecipe);
        assertEquals(request.recipeName(), savedRecipe.getRecipeName());
        assertEquals(request.category(), savedRecipe.getCategory());
        assertEquals(request.ingredients().size(), savedRecipe.getIngredients().size());
        assertEquals(request.instructions(), savedRecipe.getInstructions());
    }

    @Test
    void createRecipeWhenNameIsNull() {
        assertThrows(RecipeValidationException.class, () -> readRecipeService.findRecipeByName(null));
    }

    @Test
    void createRecipeWhenNameIsEmpty() {
        assertThrows(RecipeValidationException.class, () -> readRecipeService.findRecipeByName(""));
    }

    @Test
    void createRecipeWhenCategoryIsNull() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", null,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe category cannot be null.", exception.getMessage());
    }

    @Test
    void createRecipeWhenIngredientsAreEmpty() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(), "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe ingredients cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createRecipeWhenInstructionsAreBlank() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe instructions cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createRecipeWhenNumberOfServingsIsNegative() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", -2, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe number of servings cannot be negative.", exception.getMessage());
    }

    @Test
    void updateRecipe() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        Recipe originalRecipe = recipeService.createRecipe(createRequest);

        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(originalRecipe.getRecipeName(),
                originalRecipe.getCategory(), List.of(ingredientService.createIngredient(new ProductName("Milk"), 200, Unit.G)),
                "Mix well", 4, List.of("Updated"));

        Recipe updatedRecipe = recipeService.updateRecipe(updateRequest);

        assertNotNull(updatedRecipe);
        assertEquals(updateRequest.recipeName(), updatedRecipe.getRecipeName());
        assertEquals(updateRequest.category(), updatedRecipe.getCategory());
        assertEquals(updateRequest.ingredients().size(), updatedRecipe.getIngredients().size());
        assertEquals(updateRequest.instructions(), updatedRecipe.getInstructions());
        assertEquals(updateRequest.numberOfServings(), updatedRecipe.getNumberOfServings());
        assertEquals(updateRequest.tags(), updatedRecipe.getTags());
    }

    @Test
    void updateRecipeWhenNotExist() {
        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Unknown Recipe", Category.DESSERT, List.of(ingredientService.createIngredient(new ProductName("Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(request));
    }

    @Test
    void updateRecipeWhenNameIsBlank() {
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(
                "", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.updateRecipe(updateRequest));

        assertEquals("Recipe name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateRecipeWhenCategoryIsNull() {
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(
                "Pancakes", null,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.updateRecipe(updateRequest));

        assertEquals("Recipe category cannot be null.", exception.getMessage());
    }

    @Test
    void updateRecipeWhenIngredientsAreEmpty() {
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(), "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.updateRecipe(updateRequest));

        assertEquals("Recipe ingredients cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateRecipeWhenInstructionsAreBlank() {
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.updateRecipe(updateRequest));

        assertEquals("Recipe instructions cannot be null or empty.", exception.getMessage());
    }

    @Test
    void updateRecipeWhenNumberOfServingsIsNegative() {
        UpdateRecipeRequest updateRequest = new UpdateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", -2, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.updateRecipe(updateRequest));

        assertEquals("Recipe number of servings cannot be negative.", exception.getMessage());
    }

    @Test
    void deleteRecipeById() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));
        Recipe recipe = recipeService.createRecipe(createRequest);

        DeleteRecipeRequest deleteRequest = new DeleteRecipeRequest(recipe.getRecipeName());

        recipeService.deleteRecipe(deleteRequest);

        assertTrue(recipeRepository.findRecipeById(recipe.getRecipeId()).isEmpty());
    }

    @Test
    void deleteRecipeWhenNotExist() {
        DeleteRecipeRequest deleteRequest = new DeleteRecipeRequest("NonExistingRecipe");

        Exception exception = assertThrows(RecipeNotFoundException.class,
                () -> recipeService.deleteRecipe(deleteRequest));

        assertEquals("Recipe with given name does not exist.", exception.getMessage());
    }

    @Test
    void deleteRecipeWhenNameIsBlank() {
        DeleteRecipeRequest deleteRequest = new DeleteRecipeRequest("");

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.deleteRecipe(deleteRequest));

        assertEquals("Recipe cannot be null", exception.getMessage());
    }

    @Test
    void deleteRecipeWhenNameIsNull() {
        DeleteRecipeRequest deleteRequest = new DeleteRecipeRequest(null);

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.deleteRecipe(deleteRequest));

        assertEquals("Recipe cannot be null", exception.getMessage());
    }
}
