package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipe;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
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
import java.util.UUID;

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

        CreateRecipe request = new CreateRecipe("Pancakes", Category.DESSERT,
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
        CreateRecipe request = new CreateRecipe(
                "Pancakes", null,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe category cannot be null.", exception.getMessage());
    }

    @Test
    void createRecipeWhenInstructionsAreBlank() {
        CreateRecipe recipe = new CreateRecipe(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "", 4, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(recipe));

        assertEquals("Recipe instructions cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createRecipeWhenNumberOfServingsIsNegative() {
        CreateRecipe request = new CreateRecipe(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", -2, List.of("Easy"));

        Exception exception = assertThrows(RecipeValidationException.class,
                () -> recipeService.createRecipe(request));

        assertEquals("Recipe number of servings cannot be negative.", exception.getMessage());
    }

    @Test
    void updateRecipe() {
        CreateRecipe createRecipe = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        Recipe originalRecipe = recipeService.createRecipe(createRecipe);

        UpdateRecipeCase updateRecipe = new UpdateRecipeCase("Updated Pancakes", null,null,
                "Updated Instructions",5,null );

        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRecipe);


        assertEquals("Updated Pancakes", updatedRecipe.getRecipeName());
        assertEquals(originalRecipe.getCategory(), updatedRecipe.getCategory());
        assertEquals(originalRecipe.getIngredients(), updatedRecipe.getIngredients());
        assertEquals("Updated Instructions", updatedRecipe.getInstructions());
        assertEquals(5, updatedRecipe.getNumberOfServings());
        assertEquals(originalRecipe.getTags(), updatedRecipe.getTags());
    }

    @Test
    void updateRecipeWhenNotExist() {
        UpdateRecipeCase request = new UpdateRecipeCase("New Recipe", Category.DESSERT,
                List.of(new Ingredient(Product.newProduct(UUID.randomUUID(), new ProductName("Cheese")), 12, Unit.DAG)),
                "Mix and cook", 2, List.of("Healthy"));

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(UUID.randomUUID(),request));
    }

    @Test
    void updateRecipeWhenNumberOfServingsIsNegative() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        Recipe originalRecipe = recipeService.createRecipe(createRequest);

        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", -2, List.of("Easy"));
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRequest);

        assertEquals(2, updatedRecipe.getNumberOfServings());
    }

    @Test
    void updateRecipeWhenNumberOfServingsIsCalculate() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG),
                        ingredientService.createIngredient(new ProductName("Eggs"), 2, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        Recipe originalRecipe = recipeService.createRecipe(createRequest);

        UpdateRecipeCase updateRequest = new UpdateRecipeCase("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 700, Unit.G)),
                "Mix ingredients and fry.", 0, List.of("Easy"));
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRequest);

        assertEquals(2, updatedRecipe.getNumberOfServings());
    }

        @Test
    void deleteRecipeById() {
        CreateRecipe createRecipe = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));
        Recipe originalRecipe = recipeService.createRecipe(createRecipe);
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToDelete = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());

        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(recipeToDelete.getRecipeId(),recipeToDelete.getRecipeName());

        recipeService.deleteRecipe(deleteRequest);

        assertTrue(recipeRepository.findRecipeById(recipeToDelete.getRecipeId()).isEmpty());
    }

    @Test
    void deleteRecipeWhenNotExist() {
        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(UUID.randomUUID(),"Non Existing Recipe");

        Exception exception = assertThrows(RecipeNotFoundException.class,
                () -> recipeService.deleteRecipe(deleteRequest));

        assertEquals("Recipe with given name does not exist.", exception.getMessage());
    }
}
