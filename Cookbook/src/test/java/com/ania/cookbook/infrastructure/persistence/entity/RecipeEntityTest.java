package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.ania.cookbook.domain.model.Ingredient.newIngredient;
import static com.ania.cookbook.domain.model.Product.newProduct;
import static org.junit.jupiter.api.Assertions.*;


class RecipeEntityTest {
    @Test
    void testNewRecipeEntity() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT, Category.VEGAN);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(null,ProductEntity.newProductEntity(UUID.randomUUID(), "Sugar"),20f, Unit.DAG,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        RecipeEntity recipe = RecipeEntity.newRecipeEntity(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

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
    void testNewRecipeEntityNullId() {
        String recipeName = "Invalid Recipe";
        List<Category> categories = List.of(Category.MAIN_DISH);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(null, ProductEntity.newProductEntity(UUID.randomUUID(), "Chicken"),200f, Unit.DAG,null));
        String instructions = "Cook chicken thoroughly";
        int numberOfServings = 2;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RecipeEntity.newRecipeEntity(null, recipeName, categories, ingredients, instructions, numberOfServings));

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
                () -> RecipeEntity.newRecipeEntity(recipeId, null,categories, ingredients,instructions, numberOfServings));
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
                () -> RecipeEntity.newRecipeEntity(recipeId, "",categories, ingredients,instructions, numberOfServings));
        assertEquals("Recipe name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyCategoriesAndIngredients() {
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(
                UUID.randomUUID(),
                "Empty Recipe",
                List.of(),
                List.of(),
                "No instructions",
                5
        );

        assertTrue(recipe.getCategories().isEmpty());
        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void testSingleCategoryAndIngredient() {
        Category category = Category.DESSERT;
        Ingredient ingredient = newIngredient(newProduct(UUID.randomUUID(), "Butter"),null,500,Unit.G,null);

        RecipeEntity recipe = RecipeEntity.newRecipeEntity(
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
                () -> RecipeEntity.newRecipeEntity(recipeId, recipeName,categories, ingredients,null, numberOfServings));
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
                () -> RecipeEntity.newRecipeEntity(recipeId, recipeName,categories, ingredients," ", numberOfServings));
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
                () -> RecipeEntity.newRecipeEntity(recipeId, recipeName,categories, ingredients,instructions,-10));
        assertEquals("Recipe number of servings cannot be negative", exception.getMessage());
    }

    @Test
    void testGetters() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Getters Recipe";
        List<Category> categories = List.of(Category.MAIN_DISH);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(null, ProductEntity.newProductEntity(UUID.randomUUID(), "Flour"),300f,Unit.G,null));
        String instructions = "Mix and fry";
        int numberOfServings = 3;

        RecipeEntity recipe = RecipeEntity.newRecipeEntity(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

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
        String recipeName = "String Test Recipe";
        List<Category> categories = List.of(Category.SOUP);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(null, ProductEntity.newProductEntity(UUID.randomUUID(), "Tomatoes"),250f,Unit.G,null));
        String instructions = "Blend and heat";
        Instant created = Instant.now();
        int numberOfServings = 2;

        RecipeEntity recipe = RecipeEntity.newRecipeEntity(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        String expected = "RecipeEntity{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", categories=" + categories +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", created=" + created +
                ", numberOfServings=" + numberOfServings +
                '}';

        assertEquals(expected, recipe.toString());
    }
}

