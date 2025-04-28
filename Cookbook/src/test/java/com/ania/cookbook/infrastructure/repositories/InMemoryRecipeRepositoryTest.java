package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
class InMemoryRecipeRepositoryTest {

    private InMemoryRecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
    }

    @Test
    void testSaveRecipe() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id,"Pasta Carbonara", null, null, "Cook pasta and mix with sauce", 6);

        RecipeEntity savedRecipe = recipeRepository.saveRecipe(recipe);

        assertNotNull(savedRecipe);
        assertEquals("Pasta Carbonara", savedRecipe.getRecipeName());
        assertTrue(recipeRepository.findRecipeById(recipe.getRecipeId()).isPresent());
    }

    @Test
    void testFindRecipeById() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id, "Chocolate Cake", null, null, "Mix ingredients and bake", 6);
        recipeRepository.saveRecipe(recipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeById(recipe.getRecipeId());

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
    }

    @Test
    void testFindRecipeById_WhenNotExists() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeById(nonExistentId);

        assertTrue(foundRecipe.isEmpty());
    }

    @Test
    void testFindRecipeByName() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id, "Pizza Margherita", null, null, "Bake in oven at high temperature", 4);
        recipeRepository.saveRecipe(recipe);


        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByName("Pizza Margherita");

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe.getRecipeId(), foundRecipe.get().getRecipeId());
        assertEquals("Pizza Margherita", foundRecipe.get().getRecipeName());
    }

    @Test
    void testFindRecipeByName_WhenNotExists() {
        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByName("NonExistentRecipe");

        assertTrue(foundRecipe.isEmpty());
    }
    @Test
    void testFindRecipeByCategoryContains() {
        UUID id = UUID.randomUUID();
        Category category = Category.DESSERT;
        List<Category> categoryList = List.of(category);
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id,"Chocolate Cake", categoryList,null,"Bake in oven at high temperature", 10);

        recipeRepository.saveRecipe(recipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByCategoryContains(categoryList.getFirst().toString());

        assertTrue(foundRecipe.isPresent());
        assertEquals("Chocolate Cake", foundRecipe.get().getRecipeName());
        assertEquals(category, foundRecipe.get().getCategories().getFirst());
    }

    @Test
    void testFindRecipeByCategoryContains_NotFound() {
        String nonExistentCategory = "Soups";

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByCategoryContains(nonExistentCategory);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByCategoryContains_NullCategory() {

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByCategoryContains(null);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByIngredientContains() {
        UUID id = UUID.randomUUID();
        ProductEntity productEntity = ProductEntity.newProductEntity(id, "chocolate");
        Ingredient ingredient = Ingredient.newIngredient(null, productEntity, 100, Unit.G,null );
        List<Ingredient> ingredients = List.of(ingredient);
        Category category = Category.DESSERT;
        List<Category> categoryList = List.of(category);

        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id,"Chocolate Cake", categoryList, ingredients,"Opis",6);

        recipeRepository.saveRecipe(recipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByIngredientContains("chocolate");

        assertTrue(foundRecipe.isPresent());
        assertEquals("Chocolate Cake", foundRecipe.get().getRecipeName());
        assertTrue(foundRecipe.get().getIngredients().contains(ingredient));
    }

    @Test
    void testFindRecipeByIngredientContains_NotFound() {
        String nonExistentIngredient = "Vanilla";

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByIngredientContains(nonExistentIngredient);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByIngredientContains_NullIngredient() {

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByIngredientContains(null);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByProduct() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = ProductEntity.newProductEntity(productId, "chocolate");
        Ingredient ingredient = Ingredient.newIngredient(null, product, 100, Unit.G, null);
        List<Ingredient> ingredients = List.of(ingredient);
        Category category = Category.DESSERT;
        List<Category> categoryList = List.of(category);
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(productId, "Chocolate Cake", categoryList, ingredients, "Opis", 6);

        recipeRepository.saveRecipe(recipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByProduct(product);

        assertTrue(foundRecipe.isPresent());
        assertEquals("Chocolate Cake", foundRecipe.get().getRecipeName());
        assertTrue(foundRecipe.get().getIngredients().contains(ingredient));
    }

    @Test
    void testFindRecipeByProduct_NotFound() {
        ProductEntity nonExistentProduct = ProductEntity.newProductEntity(UUID.randomUUID(), "Vanilla");

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByProduct(nonExistentProduct);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByCreatedAfter_WhenExists() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id,"Tomato Soup", null, null, "Boil tomatoes and blend",  3);
        recipeRepository.saveRecipe(recipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeByCreatedAfter(now.minusSeconds(10));

        assertTrue(foundRecipe.isPresent());
    }

    @Test
    void testUpdateRecipe() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id, "Lasagna", null, null, "Layer and bake", 5);
        recipeRepository.saveRecipe(recipe);

        RecipeEntity updatedRecipe = RecipeEntity.newRecipeEntity(id, "Lasagna - Updated", null, null, "Layer, bake, and add cheese", 6);
        recipeRepository.updateRecipe(updatedRecipe);

        Optional<RecipeEntity> foundRecipe = recipeRepository.findRecipeById(updatedRecipe.getRecipeId());

        assertTrue(foundRecipe.isPresent());
        assertEquals("Lasagna - Updated", foundRecipe.get().getRecipeName());
    }

    @Test
    void testDeleteRecipeById() {
        UUID id = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.newRecipeEntity(id, "French Fries", null, null, "Fry potatoes", 7);
        recipeRepository.saveRecipe(recipe);

        recipeRepository.deleteRecipeById(recipe.getRecipeId());

        assertFalse(recipeRepository.findRecipeById(recipe.getRecipeId()).isPresent());
    }
}