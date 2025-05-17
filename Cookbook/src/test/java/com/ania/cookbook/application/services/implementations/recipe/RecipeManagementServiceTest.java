package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecipeManagementServiceTest {
    private RecipeManagementService recipeManagementService;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private ReadRecipeService readRecipeService;

    @BeforeEach
    void setUp() {
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        recipeManagementService = new RecipeManagementService(recipeRepository);
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        ingredientService = new IngredientService(productService);
        readRecipeService = new ReadRecipeService(recipeRepository);
    }

    @Test
    void findRecipesByName() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));

        Recipe recipe = recipeService.createRecipe(createRequest);
        List<Recipe> foundRecipes = recipeManagementService.findRecipesByName(recipe.getRecipeName());

        assertFalse(foundRecipes.isEmpty());
        assertEquals("Pancakes", foundRecipes.getFirst().getRecipeName());

    }

    @Test
    void findRecipeWhenRecipeNotFound() {
        String recipeName  = "NonExistingRecipe";

        assertThrows(RecipeNotFoundException.class, () -> readRecipeService.findRecipeByName(recipeName));
    }

    @Test
    void findRecipeHandleCaseInsensitiveSearch() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        recipeService.createRecipe(createRequest);

        List<Recipe> foundRecipes = readRecipeService.findRecipeByName("pancakes");

        assertFalse(foundRecipes.isEmpty());
        assertEquals("Pancakes", foundRecipes.getFirst().getRecipeName());
    }


    @Test
    void addRecipeByChoice() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        CreateRecipeRequest createRequest2 = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Milk"), 10, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        Recipe recipe2 = recipeService.createRecipe(createRequest2);
        List<Recipe> matchingRecipes = List.of(recipe, recipe2);

        boolean success = recipeManagementService.addRecipeByChoice(1, matchingRecipes);
        List<Recipe> recipeList = recipeManagementService.getRecipeList();

        assertTrue(success);
        assertEquals(1, recipeList.size());
        assertEquals("Pancakes", recipeList.getFirst().getRecipeName());
    }

    @Test
    void addRecipeWhenChoiceIsOutOfBounds() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        List<Recipe> matchingRecipes = List.of(recipe);

        boolean successLow = recipeManagementService.addRecipeByChoice(0, matchingRecipes);
        boolean successHigh = recipeManagementService.addRecipeByChoice(2, matchingRecipes);

        assertFalse(successLow);
        assertFalse(successHigh);
        assertTrue(recipeManagementService.getRecipeList().isEmpty());
    }

    @Test
    void addRecipeWhenListIsEmpty() {
        boolean success = recipeManagementService.addRecipeByChoice(1, List.of());

        assertFalse(success);
        assertTrue(recipeManagementService.getRecipeList().isEmpty());
    }

    @Test
    void getRecipeList() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        CreateRecipeRequest createRequest2 = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Milk"), 10, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        Recipe recipe2 = recipeService.createRecipe(createRequest2);
        recipeManagementService.addRecipeByChoice(1, List.of(recipe, recipe2));
        recipeManagementService.saveRecipeList();

        List<Recipe> recipeList = recipeManagementService.getRecipeList();

        assertFalse(recipeList.isEmpty());
        assertEquals("Pancakes", recipeList.getFirst().getRecipeName());
        assertEquals(1, recipeList.size());
    }

    @Test
    void getEmptyRecipeList() {
        List<Recipe> recipeList = recipeManagementService.getRecipeList();

        assertNotNull(recipeList);
        assertTrue(recipeList.isEmpty());
    }

    @Test
    void getNewRecipeListInstance() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeByChoice(1,List.of(recipe));

        List<Recipe> recipeList1 = recipeManagementService.getRecipeList();
        List<Recipe> recipeList2 = recipeManagementService.getRecipeList();
        recipeList1.clear();

        assertFalse(recipeList2.isEmpty());
    }


    @Test
    void saveRecipeList() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeByChoice(1,List.of(recipe));

        recipeManagementService.saveRecipeList();
        List<Recipe> foundRecipes = recipeManagementService.findRecipesByName("Pancakes");

        assertFalse(foundRecipes.isEmpty());
        assertEquals("Pancakes", foundRecipes.getFirst().getRecipeName());
        assertEquals(1, foundRecipes.size());
        assertEquals(1, recipeManagementService.getRecipeList().size());
    }



    @Test
    void removeRecipeFromList() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeByChoice(1,List.of(recipe));

        boolean removed = recipeManagementService.removeRecipeFromList("Pancakes", 1);

        assertTrue(removed);
        assertTrue(recipeManagementService.getRecipeList().isEmpty());
    }

    @Test
    void removeRecipeIfChoiceOutOfBounds() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeByChoice(1,List.of(recipe));

        boolean removedLow = recipeManagementService.removeRecipeFromList("Pancakes", 0);
        boolean removedHigh = recipeManagementService.removeRecipeFromList("Pancakes", 2);

        assertFalse(removedLow);
        assertFalse(removedHigh);
        assertEquals(1, recipeManagementService.getRecipeList().size());
    }


    @Test
    void clearRecipeList() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        CreateRecipeRequest createRequest2 = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Milk"), 10, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        Recipe recipe2 = recipeService.createRecipe(createRequest2);
        recipeManagementService.addRecipeByChoice(1, List.of(recipe, recipe2));
        recipeManagementService.addRecipeByChoice(2, List.of(recipe, recipe2));

        boolean cleared = recipeManagementService.clearRecipeList(true);

        assertTrue(cleared);
        assertTrue(recipeManagementService.getRecipeList().isEmpty());
    }

    @Test
    void notClearRecipeListWithoutConfirmation() {
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductUseCase.ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeByChoice(1,List.of(recipe));

        boolean cleared = recipeManagementService.clearRecipeList(false);

        // Then
        assertFalse(cleared);
        assertEquals(1, recipeManagementService.getRecipeList().size());
    }
}