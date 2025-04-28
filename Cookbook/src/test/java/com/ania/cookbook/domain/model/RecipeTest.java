package com.ania.cookbook.domain.model;

import static com.ania.cookbook.domain.model.Ingredient.newIngredient;
import static com.ania.cookbook.domain.model.Product.newProduct;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class RecipeTest {
    @Test
    void testNewRecipe() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        assertNotNull(recipe);
        assertEquals(recipeId, recipe.getRecipeId());
        assertEquals(recipeName, recipe.getRecipeName());
        assertEquals(categories, recipe.getCategories());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(numberOfServings, recipe.getNumberOfServings());
        assertNotNull(recipe.getCreated());
    }

    @Test
    void testNewRecipeNullId() {
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(null, recipeName,categories, ingredients,instructions, numberOfServings));
                assertEquals("Recipe id cannot be null", exception.getMessage());
    }

    @Test
    void testNewRecipeNullName() {
        UUID recipeId = UUID.randomUUID();
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(recipeId, null,categories, ingredients,instructions, numberOfServings));
        assertEquals("Recipe name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyName() {
        UUID recipeId = UUID.randomUUID();
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(recipeId, "",categories, ingredients,instructions, numberOfServings));
        assertEquals("Recipe name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyCategoriesAndIngredients() {
        Recipe recipe = Recipe.newRecipe(
                UUID.randomUUID(),
                "Empty Recipe",
                List.of(),
                List.of(),
                "No instructions",
                1
        );

        assertTrue(recipe.getCategories().isEmpty());
        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void testSingleCategoryAndIngredient() {
        Category category = Category.DESSERT;
        Ingredient ingredient = newIngredient(newProduct(UUID.randomUUID(), "Butter"),null,500,Unit.G,null);

        Recipe recipe = Recipe.newRecipe(
                UUID.randomUUID(),
                "Butter Cookies",
                List.of(category),
                List.of(ingredient),
                "Mix and bake",
                6
        );

        assertEquals(1, recipe.getCategories().size());
        assertEquals(category, recipe.getCategories().getFirst());
        assertEquals(1, recipe.getIngredients().size());
        assertEquals(ingredient, recipe.getIngredients().getFirst());
    }

    @Test
    void testNewRecipeNullInstruction() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        int numberOfServings = 4;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,categories, ingredients,null, numberOfServings));
        assertEquals("Recipe instructions cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeEmptyInstruction() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        int numberOfServings = 4;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,categories, ingredients," ", numberOfServings));
        assertEquals("Recipe instructions cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNewRecipeInvalidNumberOfServings() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(Product.newProduct(UUID.randomUUID(), "Sugar"),null,500, Unit.G,null));
        String instructions = "Mix everything and bake";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Recipe.newRecipe(recipeId, recipeName,categories, ingredients,instructions,-10));
        assertEquals("Recipe number of servings cannot be negative", exception.getMessage());
    }

    @Test
    void testGetters() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.ITALIAN);
        List<Ingredient> ingredients = List.of(newIngredient(newProduct(UUID.randomUUID(), "Chicken"),null,200f, Unit.DAG, null));
        String instructions = "Cook chicken thoroughly";
        int numberOfServings = 2;

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        assertEquals(recipeId, recipe.getRecipeId());
        assertEquals(recipeName, recipe.getRecipeName());
        assertEquals(categories, recipe.getCategories());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(numberOfServings, recipe.getNumberOfServings());
        assertNotNull(recipe.getCreated());
    }

    @Test
    void testToString() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Pancakes";
        List<Category> categories = List.of(Category.DESSERT);
        List<Ingredient> ingredients = List.of(newIngredient(newProduct(UUID.randomUUID(), "Flour"),null, 300f,Unit.G,null));
        String instructions = "Mix and fry";
        int numberOfServings = 3;

        Recipe recipe = Recipe.newRecipe(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        String expected = "Recipe{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", categories=" + categories +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", created=" + recipe.getCreated() +
                ", numberOfServings=" + numberOfServings +
                '}';

        assertEquals(expected, recipe.toString());
    }


}
