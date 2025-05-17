package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {
    private IngredientService ingredientService;
    private RecipeService recipeService;
    private ShoppingListService shoppingListService;
    @BeforeEach
    void setUp() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        ingredientService = new IngredientService(productService);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
        shoppingListService = new ShoppingListService();
    }

    @Test
    void generateShoppingList() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName("Milk"), 200, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Eggs"), 2, Unit.DAG);
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);
        Ingredient ingredient3 = ingredientService.createIngredient(new ProductName("Milk"), 3, Unit.DAG);
        Ingredient ingredient4 = ingredientService.createIngredient(new ProductName("Flour"), 20, Unit.DAG);
        List<Ingredient> ingredients2 = List.of(ingredient3, ingredient4);
        CreateRecipeRequest createRequest = new CreateRecipeRequest("Pancakes", Category.DESSERT,
              ingredients, "Mix and cook", 2, List.of("Easy", "Breakfast"));
        CreateRecipeRequest createRequest2 = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                ingredients2, "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe recipe1 = recipeService.createRecipe(createRequest);
        Recipe recipe2 = recipeService.createRecipe(createRequest2);

        List<Recipe> recipes = List.of(recipe1, recipe2);

        Map<String, Float> shoppingList = shoppingListService.generateShoppingList(recipes);

        assertEquals(3, shoppingList.size());
        assertEquals(230, shoppingList.get("Milk"));
        assertEquals(20, shoppingList.get("Eggs"));
        assertEquals(200, shoppingList.get("Flour"));
    }

    @Test
    void generateShoppingListEmptyRecipes() {
        List<Recipe> recipes = List.of();
        Map<String, Float> shoppingList = shoppingListService.generateShoppingList(recipes);
        assertTrue(shoppingList.isEmpty());
    }

    @Test
    void generateShoppingListWhenDuplicateProducts() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName("Milk"), 500, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Milk"), 200, Unit.G);

        CreateRecipeRequest request = new CreateRecipeRequest(
                "Pancakes", Category.DESSERT,
                List.of(ingredient1, ingredient2), "Mix ingredients and fry.",
                4, List.of("Easy"));
        Recipe savedRecipe = recipeService.createRecipe(request);
        Map<String, Float> shoppingList = shoppingListService.generateShoppingList(List.of(savedRecipe));

        assertEquals(1, shoppingList.size());
        assertEquals(700, shoppingList.get("Milk"));
    }
}