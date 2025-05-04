package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadRecipeServiceTest {
    @Mock
    private ReadRecipe readRecipeRepository;

    @InjectMocks
    private ReadRecipeService readRecipeService;

    private Recipe recipe;
    private UUID recipeId;
    private String recipeName;
    private Category category;
    private Instant created;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        recipeName = "Spaghetti Carbonara";
        category = Category.MAIN_DISH;
        created = Instant.now();
        UUID productId = UUID.randomUUID();
        UUID product2Id = UUID.randomUUID();
        Ingredient ingredient = Ingredient.newIngredient(Product.newProduct(productId,"pasta"),500,MassUnit.G);
        Ingredient ingredient2 = Ingredient.newIngredient(Product.newProduct(product2Id,"sauce"),50,MassUnit.DAG);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        ingredients.add(ingredient2);
        String instructions = "Cook pasta and mix with sauce";
        recipe = Recipe.newRecipe(recipeId,recipeName,category,ingredients, instructions,5,List.of("Italian","Pasta"));
    }

    @Test
    void testFindRecipeById() {
        when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeById(recipeId);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
    }

    @Test
    void testFindRecipeByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(readRecipeRepository.findRecipeById(id)).thenReturn(Optional.empty());
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeById(id);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeById(id);
    }

    @Test
    void testExistsRecipeById() {
        when(readRecipeRepository.existsRecipeById(recipeId)).thenReturn(true);
        assertTrue(readRecipeService.existsRecipeById(recipeId));
    }

    @Test
    void testExistsRecipeByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(readRecipeRepository.existsRecipeById(nonExistentId)).thenReturn(false);

        assertFalse(readRecipeService.existsRecipeById(nonExistentId));
        verify(readRecipeRepository, times(1)).existsRecipeById(nonExistentId);
    }

    @Test
    void testFindRecipeByName() {
        when(readRecipeRepository.findRecipeByName(recipeName)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByName(recipeName);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(readRecipeRepository, times(1)).findRecipeByName(recipeName);
    }

    @Test
    void testFindRecipeByNameNotFound() {
        String name = "Nonexistent Recipe";
        when(readRecipeRepository.findRecipeByName(name)).thenReturn(Optional.empty());
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByName(name);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeByName(name);
    }
    @Test
    void testExistsRecipeByName() {
        when(readRecipeRepository.existsRecipeByName(recipeName)).thenReturn(true);
        assertTrue(readRecipeService.existsRecipeByName(recipeName));
    }

    @Test
    void testExistsRecipeByNameNotFound() {
        String nonExistentRecipeName = "Nonexistent Recipe";
        when(readRecipeRepository.existsRecipeByName(nonExistentRecipeName)).thenReturn(false);
        boolean recipeExists = readRecipeService.existsRecipeByName(nonExistentRecipeName);

        assertFalse(recipeExists);
        verify(readRecipeRepository, times(1)).existsRecipeByName(nonExistentRecipeName);
    }

    @Test
    void testFindRecipeByCategory() {
        when(readRecipeRepository.findRecipeByCategory(category)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByCategory(category);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
    }
    @Test
    void testFindRecipeByCategoryNotFound() {
        Category nonExistingCategory = Category.DESSERT;
        when(readRecipeRepository.findRecipeByCategory(nonExistingCategory)).thenReturn(Optional.empty());

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByCategory(nonExistingCategory);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeByCategory(nonExistingCategory);
    }

    @Test
    void testFindRecipeByIngredient() {
      String ingredient = "flour";
        when(readRecipeRepository.findRecipeByIngredientContains(ingredient)).thenReturn(Optional.of(recipe));

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByIngredient(ingredient);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(readRecipeRepository, times(1)).findRecipeByIngredientContains(ingredient);
    }

    @Test
    void testFindRecipeByIngredientNotFound() {
        String nonExistentIngredient = "Nonexistent Ingredient";

        when(readRecipeRepository.findRecipeByIngredientContains(nonExistentIngredient)).thenReturn(Optional.empty());

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByIngredient(nonExistentIngredient);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeByIngredientContains(nonExistentIngredient);
    }

    @Test
    void testFindRecipeByProductIdFound() {
        UUID productId = recipe.getIngredients().getFirst().getProduct().getProductId();
        when(readRecipeRepository.findRecipeByProductId(productId)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByProductId(productId);
        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());

        verify(readRecipeRepository, times(1)).findRecipeByProductId(recipe.getIngredients().getFirst().getProduct().getProductId());
    }

    @Test
    void testFindRecipeByProductIdNotFound() {
        UUID nonExistentProductId = UUID.randomUUID();
        when(readRecipeRepository.findRecipeByProductId(nonExistentProductId)).thenReturn(Optional.empty());
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByProductId(nonExistentProductId);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeByProductId(nonExistentProductId);
    }

    @Test
    void testFindRecipeByProductNameFound() {
        String productName = recipe.getIngredients().getFirst().getProduct().getProductName();
        when(readRecipeRepository.findRecipeByProductName(productName)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByProductName(productName);
        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());

        verify(readRecipeRepository, times(1)).findRecipeByProductName(recipe.getIngredients().getFirst().getProduct().getProductName());
    }

    @Test
    void testFindRecipeByProductNameNotFound() {
        String nonExistentProductName = "Nonexistent Product";
        when(readRecipeRepository.findRecipeByProductName(nonExistentProductName)).thenReturn(Optional.empty());

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByProductName(nonExistentProductName);
        assertFalse(foundRecipe.isPresent());

        verify(readRecipeRepository, times(1)).findRecipeByProductName(nonExistentProductName);
    }


    @Test
    void testFindRecipeByCreatedAfter() {
        when(readRecipeRepository.findRecipeByCreatedAfter(created)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByCreatedAfter(created);

        assertTrue(foundRecipe.isPresent());
        assertEquals(recipe, foundRecipe.get());
        verify(readRecipeRepository, times(1)).findRecipeByCreatedAfter(created);
    }
    @Test
    void testFindRecipeByCreatedAfterNotFound() {
        Instant date = Instant.now();

        when(readRecipeRepository.findRecipeByCreatedAfter(date)).thenReturn(Optional.empty());

        Optional<Recipe> foundRecipe = readRecipeService.findRecipeByCreatedAfter(date);

        assertFalse(foundRecipe.isPresent());
        verify(readRecipeRepository, times(1)).findRecipeByCreatedAfter(date);
    }
}