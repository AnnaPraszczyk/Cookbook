package com.ania.cookbook.domain.model;

import static com.ania.cookbook.domain.model.Ingredient.newIngredient;
import static com.ania.cookbook.domain.model.Product.newProduct;
import static org.junit.jupiter.api.Assertions.*;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class RecipeTest {
    @Test
    void testNewRecipe() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),15.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("polish", "vegan");

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, category, ingredients, instructions, numberOfServings,tags);

        assertNotNull(recipe);
        assertEquals(recipeId, recipe.getRecipeId());
        assertEquals(recipeName, recipe.getRecipeName());
        assertEquals(category, recipe.getCategory());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(instructions, recipe.getInstructions());
        assertNotNull(recipe.getCreated());
        assertEquals(numberOfServings, recipe.getNumberOfServings());
        assertEquals(tags, recipe.getTags());
    }

    @Test
    void testNewRecipeNullId() {
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),15.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("polish", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(null, recipeName,category, ingredients,instructions, numberOfServings,tags));
        assertEquals("Recipe id cannot be null", exception.getMessage());
    }

    @Test
    void testNewRecipeNullName() {
        UUID recipeId = UUID.randomUUID();
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),15.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("polish", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId,null, category, ingredients,instructions, numberOfServings,tags));
        assertEquals("Recipe name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyName() {
        UUID recipeId = UUID.randomUUID();
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),15.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("polish", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId, "",category, ingredients,instructions, numberOfServings,tags));
        assertEquals("Recipe name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeNullCategory() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),15.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("mexican", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,null, ingredients,instructions, numberOfServings,tags));
        assertEquals("Recipe category cannot be null", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyIngredients() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("mexican", "vegan");

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, Category.DESSERT, List.of(), instructions, numberOfServings,tags);

        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void testSingleIngredient() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Ingredient ingredient = newIngredient(newProduct(UUID.randomUUID(), "Butter"),15.0f, Unit.G);
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("mexican", "vegan");

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, Category.DESSERT, List.of(ingredient), instructions, numberOfServings,tags);

        assertEquals(1, recipe.getIngredients().size());
        assertEquals(ingredient, recipe.getIngredients().getFirst());
    }

    @Test
    void testNewRecipeNullInstruction() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),20.0f, Unit.G));
        int numberOfServings = 4;
        List<String> tags = List.of("italian", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,category, ingredients,null, numberOfServings,tags));
        assertEquals("Recipe instructions cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyInstruction() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),20.0f, Unit.G));
        int numberOfServings = 4;
        List<String> tags = List.of("italian", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,category, ingredients," ", numberOfServings,tags));
        assertEquals("Recipe instructions cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateDateNotNull() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),20.0f, Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;
        List<String> tags = List.of("italian", "vegan");

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName,category, ingredients,instructions, numberOfServings,tags);

        assertNotNull(recipe.getCreated());
    }

    @Test
    void testNewRecipeInvalidNumberOfServings() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),4.0f, Unit.G));
        String instructions = "Mix everything and bake";
        List<String> tags = List.of("italian", "vegan");

        RecipeValidationException exception = assertThrows(RecipeValidationException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,category, ingredients,instructions,-5,tags));
        assertEquals("Recipe number of servings cannot be negative", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyTags() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        Category category = Category.DESSERT;
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),4.0f,Unit.G));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName,category, ingredients,instructions,numberOfServings,List.of());

        assertNotNull(recipe.getTags());
        assertTrue(recipe.getTags().isEmpty());
    }

    @Test
    void CalculateServingsBasedOnIngredientMass() {
        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"),500, Unit.G),  // 500g
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Milk"), 20, Unit.DAG),   // 20g
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Eggs"), 100, Unit.G)    // 100g
        );
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT, ingredients, "Mix and fried", 0,List.of("Easy", "Breakfast" ));
        assertEquals(2, recipe.getNumberOfServings());
    }

    @Test
    void RoundDownServingsIfBelowThreshold() {
        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Butter"), 100, Unit.G),  // 100g
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Sugar"), 150, Unit.G)    // 150g
        );

        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Cake", Category.DESSERT, ingredients,"Mix and bake",0,null);
        assertEquals(0, recipe.getNumberOfServings());
    }

    @Test
    void ConvertVariousUnitsBeforeCalculatingServings() {
        List<Ingredient> ingredients = List.of(
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Rice"), 0.5f, Unit.KG),
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Chicken"), 1, Unit.LB),  // 453.592g
                Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Spices"), 50, Unit.G)    // 50g
        );

        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Rice Bowl", Category.MAIN_COURSE, ingredients, "Cook" ,0, List.of("Asian","Dinner"));
        assertEquals(2, recipe.getNumberOfServings()); // (500+453.592+50) / 350 = 2.86 → 2
    }
}
