package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipe;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListNotFoundExeption;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RecipeManagementServiceTest {
    private RecipeManagementService recipeManagementService;
    private RecipeService recipeService;
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        recipeManagementService = new RecipeManagementService(recipeRepository, recipeRepository);
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        ingredientService = new IngredientService(productService);
    }

    @Test
    void createRecipeList() {
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);

        assertNotNull(recipeManagementService.getRecipesList(listName));
        assertTrue(recipeManagementService.getRecipesList(listName).isEmpty());
    }

    @Test
    void createRecipeListWhenNameIsBlank() {
        assertThrows(ListValidationException.class,
                () -> recipeManagementService.createRecipeList(new ListName("")));
    }

    @Test
    void createRecipeListWhenNameIsNull() {
        assertThrows(ListValidationException.class,
                () -> recipeManagementService.createRecipeList(new ListName(null)));
    }

    @Test
    void addRecipeById() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        CreateRecipe createRequest2 = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Milk"), 10, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        Recipe recipe2 = recipeService.createRecipe(createRequest2);
        ListName listName = new ListName("New list");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        UUID recipeId2 = recipe2.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);
        recipeManagementService.addRecipeToList(recipeId2, listName);
        List<Recipe> newList = recipeManagementService.getRecipesList(listName);

        assertNotNull(newList);
        assertEquals(2, newList.size());
        assertEquals("Pancakes", newList.getFirst().getRecipeName());
    }

    @Test
    void addRecipeByIdWhenRecipeNotFound() {

        assertThrows(RecipeNotFoundException.class,
                () -> recipeManagementService.addRecipeToList(UUID.randomUUID(), new ListName("New List")));
    }

    @Test
    void addRecipeByIdDuplicateRecipes() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);

        recipeManagementService.addRecipeToList(recipe.getRecipeId(), listName);
        recipeManagementService.addRecipeToList(recipe.getRecipeId(), listName);

        List<Recipe> drinksList = recipeManagementService.getRecipesList(listName);

        assertEquals(1, drinksList.size());
    }

    @Test
    void saveRecipesList() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("New list");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);

        recipeManagementService.saveRecipesList(listName);

        assertEquals(1, recipeManagementService.getRecipesList(listName).size());
        assertEquals("Pancakes", recipeManagementService.getRecipesList(listName).getFirst().getRecipeName());
    }

    @Test
    void saveRecipesListWhenListNameIsBlank() {
        Exception exception = assertThrows(ListValidationException.class, () -> recipeManagementService.saveRecipesList(new ListName("")));
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void saveRecipeListWhenListNameIsNull() {

        Exception exception = assertThrows(ListValidationException.class, () -> recipeManagementService.saveRecipesList(new ListName(null)));
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getRecipesList() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);
        recipeManagementService.saveRecipesList(listName);

        List<Recipe> result = recipeManagementService.getRecipesList(listName);

        assertEquals(1, result.size());
        assertEquals("Pancakes", result.getFirst().getRecipeName());
    }

    @Test
    void getRecipesListWhenRecipeListDoesNotExist() {
        List<Recipe> result = recipeManagementService.getRecipesList(new ListName("NonExistingList"));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeRecipeFromList() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);
        recipeManagementService.saveRecipesList(listName);

        recipeManagementService.removeRecipeFromList(recipeId,listName);
        List<Recipe> recipeList = recipeManagementService.getRecipesList(listName);

        assertFalse(recipeList.contains(recipe));
        assertEquals(0, recipeList.size());
    }

    @Test
    void removeRecipeFromListWhenListNameIsBlank() {
        Exception exception = assertThrows(ListValidationException.class, () -> recipeManagementService.removeRecipeFromList(UUID.randomUUID(), new ListName("")));
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void removeRecipeFromListWhenRecipeListDoesNotExist() {
        Exception exception = assertThrows(ListNotFoundExeption.class, () -> recipeManagementService.removeRecipeFromList(UUID.randomUUID(), new ListName("NonExistingList")));
        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
    }

    @Test
    void removeRecipeFromListWhenRecipeIdIsNull() {
        ListName listName = new ListName("ExistingList");
        recipeManagementService.createRecipeList(listName);

        Exception exception = assertThrows(RecipeValidationException.class, () -> recipeManagementService.removeRecipeFromList(null, listName));
        assertEquals("Recipe ID cannot be null.", exception.getMessage());
    }

    @Test
    void removeRecipeFromListWhenRecipeListIsEmpty() {
        ListName listName = new ListName("EmptyList");
        recipeManagementService.createRecipeList(listName);

        Exception exception = assertThrows(RecipeNotFoundException.class, () -> recipeManagementService.removeRecipeFromList(UUID.randomUUID(), listName));
        assertEquals("No recipes found in the list.", exception.getMessage());
    }

    @Test
    void removeRecipeFromListWhenRecipeDoesNotExistInList() {
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        recipeManagementService.addRecipeToList(recipe.getRecipeId(), listName);

        UUID recipeId = UUID.randomUUID();

        Exception exception = assertThrows(RecipeNotFoundException.class, () -> recipeManagementService.removeRecipeFromList(recipeId, listName));
        assertEquals("Recipe with given ID does not exist in the list.", exception.getMessage());
    }

    @Test
    void clearRecipeList() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);
        recipeManagementService.saveRecipesList(listName);

        boolean result = recipeManagementService.clearRecipeList(listName, true);

        assertTrue(result);
        assertTrue(recipeManagementService.getRecipesList(listName).isEmpty());
    }

    @Test
    void clearRecipeListWhenConfirmationIsFalse() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Flour"), 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        UUID recipeId = recipe.getRecipeId();
        recipeManagementService.addRecipeToList(recipeId, listName);
        recipeManagementService.saveRecipesList(listName);

        boolean result = recipeManagementService.clearRecipeList(listName, false);

        assertFalse(result);
        assertFalse(recipeManagementService.getRecipesList(listName).isEmpty());
    }

    @Test
    void clearRecipeListWhenListNameIsBlank() {
        Exception exception = assertThrows(ListValidationException.class, () -> recipeManagementService.clearRecipeList(new ListName(""), true));
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void clearRecipeListWhenRecipeListDoesNotExist() {
        Exception exception = assertThrows(ListNotFoundExeption.class, () -> recipeManagementService.clearRecipeList(new ListName("NonExistingList"), true));
        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
    }

    @Test
    void deleteRecipeList() {
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);

        recipeManagementService.deleteRecipeList(listName);

        assertTrue(recipeManagementService.getRecipesList(listName).isEmpty());
    }

    @Test
    void deleteRecipeListWhenListNameIsBlank() {
        Exception exception = assertThrows(ListValidationException.class, () -> recipeManagementService.deleteRecipeList(new ListName("")));
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void deleteRecipeListWhenRecipeListDoesNotExist() {
        Exception exception = assertThrows(ListNotFoundExeption.class, () -> recipeManagementService.deleteRecipeList(new ListName("NonExistingList")));
        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
    }

    @Test
    void generateShoppingListAggregateIngredientsFromSingleRecipe() {
        Product product1 = Product.newProduct(UUID.randomUUID(), new ProductName("Flour"));
        Product product2 = Product.newProduct(UUID.randomUUID(), new ProductName("Sugar"));

        Ingredient ingredient1 = Ingredient.newIngredient(product1, 20, Unit.DAG);
        Ingredient ingredient2 = Ingredient.newIngredient(product1, 300, Unit.G);
        Ingredient ingredient3 = new Ingredient(product2, 10, Unit.DAG);
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredient1, ingredient2, ingredient3), "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe = recipeService.createRecipe(createRequest);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        recipeManagementService.addRecipeToList(recipe.getRecipeId(), listName);

        Map<String, Float> shoppingList = recipeManagementService.generateShoppingList(listName);

        assertEquals(500, shoppingList.get("Flour"));
        assertEquals(100, shoppingList.get("Sugar"));
    }

    @Test
    void generateShoppingListMergeIngredientsAcrossMultipleRecipes() {
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Butter"));
        Product product2 = Product.newProduct(UUID.randomUUID(), new ProductName("Sugar"));

        Ingredient ingredient1 = Ingredient.newIngredient(product, 100, Unit.G);
        Ingredient ingredient2 = Ingredient.newIngredient(product, 250, Unit.G);
        Ingredient ingredient3 = Ingredient.newIngredient(product2, 250, Unit.G);
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);
        CreateRecipe createRequest1 = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(ingredient1, ingredient2,ingredient3), "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe1 = recipeService.createRecipe(createRequest1);
        recipeManagementService.addRecipeToList(recipe1.getRecipeId(), listName);
        CreateRecipe createRequest2 = new CreateRecipe("Chocolate cake", Category.DESSERT,
                List.of(ingredient1, ingredient2,ingredient3), "Mix and cook", 6, List.of("Easy"));
        Recipe recipe2 = recipeService.createRecipe(createRequest2);
        recipeManagementService.addRecipeToList(recipe2.getRecipeId(), listName);

        Map<String, Float> shoppingList = recipeManagementService.generateShoppingList(listName);

        assertEquals(700, shoppingList.get("Butter"));
        assertEquals(500, shoppingList.get("Sugar"));
    }

    @Test
    void generateShoppingListWhenListDoesNotExist() {
        Exception exception = assertThrows(ListNotFoundExeption.class, () -> recipeManagementService.generateShoppingList(new ListName("NonExistingList")));
        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
    }

    @Test
    void generateShoppingListWhenRecipeListIsEmpty() {
        ListName listName = new ListName("Desserts");
        recipeManagementService.createRecipeList(listName);

        Map<String, Float> shoppingList = recipeManagementService.generateShoppingList(listName);

        assertTrue(shoppingList.isEmpty());
    }
}

