package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipeRequest;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeScalingServiceTest {
    private RecipeScalingService recipeScalingService;
    private IngredientService ingredientService;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        recipeScalingService = new RecipeScalingService(recipeRepository);
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository, productRepository, productRepository, productRepository);
        ingredientService = new IngredientService(productService);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository);
    }

    @Test
    void calculateServings() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName( "Flour"), 200, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName( "Milk"), 500, Unit.G);
        Ingredient ingredient3 = ingredientService.createIngredient(new ProductName( "Eggs"), 3, Unit.G);

        List<Ingredient> ingredients = List.of(ingredient1, ingredient2, ingredient3);
        int servings = recipeScalingService.calculateServings(ingredients);

        assertEquals(2, servings);
    }
    @Test
    void adjustRecipeByServings() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName( "Sugar"), 100, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Butter"), 200, Unit.G);
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);
        CreateRecipeRequest request = new CreateRecipeRequest("Pancakes", Category.DESSERT,
                ingredients, "Mix and fry", 2, List.of("Easy"));

        recipeService.createRecipe(request);

        Recipe scaledRecipe = recipeScalingService.adjustRecipeByServings(new AdjustRecipeRequest("Pancakes", 4, 1));

        assertNotNull(scaledRecipe);
        assertEquals(4, scaledRecipe.getNumberOfServings());
        assertEquals(200, scaledRecipe.getIngredients().get(0).getAmount());
        assertEquals(400, scaledRecipe.getIngredients().get(1).getAmount());
    }

    @Test
    void findRecipe() {

        CreateRecipeRequest recipe1 = new CreateRecipeRequest("Cake", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName( "Sugar"), 100, Unit.G)),
                "Bake", 2, List.of("Sweet"));
        CreateRecipeRequest recipe2 = new CreateRecipeRequest("Cake", Category.DESSERT,
                List.of(ingredientService.createIngredient(new ProductName("Butter"), 200, Unit.G)),
                "Bake", 3, List.of("Sweet"));
       recipeService.createRecipe(recipe1);
       recipeService.createRecipe(recipe2);

        Recipe foundRecipe = recipeScalingService.adjustRecipeByServings(new AdjustRecipeRequest("Cake", 4, 2));

        assertNotNull(foundRecipe);
        assertEquals(4, foundRecipe.getNumberOfServings());
    }

    @Test
    void adjustRecipeByServingsForUnknownRecipe() {
        Recipe adjustedRecipe = recipeScalingService.adjustRecipeByServings(new AdjustRecipeRequest("Unknown", 1, 3));
        assertNull(adjustedRecipe);
    }
}