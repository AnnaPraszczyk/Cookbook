package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryEntryRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryListRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class RecipeScalingServiceTest {
    private RecipeScalingService recipeScalingService;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductUseCase productUseCase = new ProductService(productRepository, productRepository, productRepository, productRepository);
        InMemoryRecipeRepository recipeRepository = new InMemoryRecipeRepository();
        InMemoryListRepository listRepository = new InMemoryListRepository();
        InMemoryEntryRepository entryRepository = new InMemoryEntryRepository();
        recipeScalingService = new RecipeScalingService(recipeRepository);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository, productUseCase, listRepository, entryRepository);
    }

    private UUID insertTestRecipe() {
        IngredientRequest ingredient = new IngredientRequest("Flour", 300F, Unit.G);
        CreateRecipe recipe = new CreateRecipe("Test", Category.DESSERT,
                List.of(ingredient), "Mix and bake", 4, List.of("fast", "easy"));
        return recipeService.createRecipe(recipe).getRecipeId();
    }

    @Test
    void adjustRecipeByServings() {
        IngredientRequest ingredient1 = new IngredientRequest( "Sugar", 100, Unit.G);
        IngredientRequest ingredient2 = new IngredientRequest("Butter", 200, Unit.G);
        List<IngredientRequest> ingredients = List.of(ingredient1, ingredient2);
        CreateRecipe recipe = new CreateRecipe("Pancakes", Category.DESSERT,
                ingredients, "Mix and fry", 2, List.of("Easy"));
        Recipe newRecipe = recipeService.createRecipe(recipe);
        Recipe scaledRecipe = recipeScalingService.adjustRecipeByServings(new AdjustRecipe(newRecipe.getRecipeId(), 4));
        Map<String, Float> scaledAmounts = scaledRecipe.getIngredients().stream()
                .collect(Collectors.toMap(i -> i.getProduct().getProductName().name(), Ingredient::getAmount));

        assertEquals(200F, scaledAmounts.get("Sugar"));
        assertEquals(400F, scaledAmounts.get("Butter"));
        assertNotNull(scaledRecipe);
        assertEquals(4, scaledRecipe.getNumberOfServings());
        assertEquals(200, scaledRecipe.getIngredients().get(0).getAmount());
        assertEquals(400, scaledRecipe.getIngredients().get(1).getAmount());
    }

    @Test
    void shouldThrowWhenCreatingRecipeWithNegativeServings() {
        IngredientRequest ingredient1 = new IngredientRequest("Sugar", 100, Unit.G);
        IngredientRequest ingredient2 = new IngredientRequest("Butter", 200, Unit.G);
        List<IngredientRequest> ingredients = List.of(ingredient1, ingredient2);

        assertThrows(RecipeValidationException.class, () -> {
            CreateRecipe recipe = new CreateRecipe("Pancakes", Category.DESSERT,
                    ingredients, "Mix and fry", -2, List.of("Easy"));
            recipeService.createRecipe(recipe);
        });
    }

    @Test
    void shouldThrowWhenScalingRecipeWithNegativeTargetServings() {
        UUID recipeId = insertTestRecipe();

        assertThrows(RecipeValidationException.class,
                () -> recipeScalingService.adjustRecipeByServings(new AdjustRecipe(recipeId, -1)));
    }

    @Test
    void shouldScaleRecipeAutomaticallyWhenServingsIsZero() {
        UUID recipeId = insertTestRecipe();

        Recipe scaled = recipeScalingService.adjustRecipeByServings(new AdjustRecipe(recipeId, 0));

        assertNotNull(scaled);
        assertEquals("Test", scaled.getRecipeName());
        assertEquals(1, scaled.getNumberOfServings());
    }

    @Test
    void shouldScaleRecipeToSingleServing() {
        UUID recipeId = insertTestRecipe();
        Recipe scaled = recipeScalingService.adjustRecipeByServings(new AdjustRecipe(recipeId, 1));

        assertEquals(1, scaled.getNumberOfServings());
        assertEquals(75F, scaled.getIngredients().getFirst().getAmount()); // 300g * 1/4
    }

    @Test
    void adjustRecipeByServingsWhenRecipeDoesNotExist() {
        AdjustRecipe recipe = new AdjustRecipe(UUID.randomUUID(), 4);
        Exception exception = assertThrows(RecipeNotFoundException.class, () -> recipeScalingService.adjustRecipeByServings(recipe));

        assertEquals("Recipe with given Id does not exist.", exception.getMessage());
    }

    @Test
    void calculateServingsBasedOnIngredientMass() {
        Product flour = Product.newProduct(UUID.randomUUID(), ProductName.from("Flour"));
        Product milk = Product.newProduct(UUID.randomUUID(), ProductName.from("Milk"));
        Ingredient flourIngredient = Ingredient.newIngredient(flour, 700f, Unit.G);
        Ingredient milkIngredient = Ingredient.newIngredient(milk, 2f, Unit.L);
        List<Ingredient> ingredients = List.of(flourIngredient, milkIngredient);
        Recipe recipe = Recipe.newRecipe(
                UUID.randomUUID(),
                "Auto-scaled Recipe",
                Category.MAIN_COURSE,
                ingredients,
                "Mix and cook",
                0,
                List.of("test")
        );

        assertNotNull(recipe);
        assertEquals(8, recipe.getNumberOfServings());
    }
}