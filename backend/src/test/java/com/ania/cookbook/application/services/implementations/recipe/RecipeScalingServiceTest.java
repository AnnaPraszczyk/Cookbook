package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipe;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
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
import java.util.UUID;
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
    void adjustRecipeByServings() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName( "Sugar"), 100, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Butter"), 200, Unit.G);
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);
        CreateRecipe recipe = new CreateRecipe("Pancakes", Category.DESSERT,
                ingredients, "Mix and fry", 2, List.of("Easy"));
        Recipe newRecipe = recipeService.createRecipe(recipe);

        Recipe scaledRecipe = recipeScalingService.adjustRecipeByServings(new AdjustRecipe(newRecipe.getRecipeId(), 4));

        assertNotNull(scaledRecipe);
        assertEquals(4, scaledRecipe.getNumberOfServings());
        assertEquals(200, scaledRecipe.getIngredients().get(0).getAmount());
        assertEquals(400, scaledRecipe.getIngredients().get(1).getAmount());
    }

    @Test
    void adjustRecipeByServingsWhenServingsIsZeroOrNegative() {
        Ingredient ingredient1 = ingredientService.createIngredient(new ProductName( "Sugar"), 100, Unit.G);
        Ingredient ingredient2 = ingredientService.createIngredient(new ProductName("Butter"), 200, Unit.G);
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);
        CreateRecipe recipe = new CreateRecipe("Pancakes", Category.DESSERT,
                ingredients, "Mix and fry", 2, List.of("Easy"));
        Recipe newRecipe = recipeService.createRecipe(recipe);

        assertThrows(RecipeValidationException.class, () -> recipeScalingService.adjustRecipeByServings(new AdjustRecipe(newRecipe.getRecipeId(), 0)));
        assertThrows(RecipeValidationException.class, () -> recipeScalingService.adjustRecipeByServings(new AdjustRecipe(newRecipe.getRecipeId(), -1)));
    }

    @Test
    void adjustRecipeByServingsWhenRecipeDoesNotExist() {

        AdjustRecipe recipe = new AdjustRecipe(UUID.randomUUID(), 4);

        Exception exception = assertThrows(RecipeNotFoundException.class, () -> recipeScalingService.adjustRecipeByServings(recipe));
        assertEquals("Recipe with given Id does not exist.", exception.getMessage());

    }
}