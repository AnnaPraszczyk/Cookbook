package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
class InMemoryRecipeRepositoryTest {

    private InMemoryRecipeRepository recipeRepository;
    private UUID recipeId;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        recipeId = UUID.randomUUID();
        Ingredient ingredient = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"flour"),200, MassUnit.G);
        Ingredient ingredient2 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"milk"), 100, VolumeUnit.ML, 100 , MassUnit.G);
        List<Ingredient> ingredients = List.of(ingredient,ingredient2);
        recipe = Recipe.newRecipe(recipeId,"Pancakes",Category.DESSERT,ingredients,"Mix all ingredients",5,List.of("Polish","Pancakes"));
    }

    @Test
    void testSaveRecipe() {
        Recipe savedRecipe = recipeRepository.saveRecipe(recipe);

        assertThat(savedRecipe).isEqualTo(recipe);
        assertThat(recipeRepository.existsRecipeById(recipeId)).isTrue();
    }

    @Test
    void testSaveRecipeThrowsValidationExceptionIfRecipeIsNull() {
        assertThatThrownBy(() -> recipeRepository.saveRecipe(null))
                .isInstanceOf(RecipeValidationException.class)
                .hasMessage("Recipe cannot be null");
    }

    @Test
    void testSaveRecipeThrowsValidationExceptionIfRecipeExists() {
        recipeRepository.saveRecipe(recipe);
        assertThatThrownBy(() -> recipeRepository.saveRecipe(recipe))
                .isInstanceOf(RecipeValidationException.class)
                .hasMessage("A recipe with the given Id already exists.");
    }

    @Test
    void testFindRecipeById() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeById(recipeId);

        assertThat(foundRecipe).isPresent().contains(recipe);
    }

    @Test
    void testFindRecipeByIdWhenNotExists() {
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeById(UUID.randomUUID());

        assertThat(foundRecipe).isEmpty();
    }

    @Test
    void testExistsRecipeById() {
        recipeRepository.saveRecipe(recipe);
        assertThat(recipeRepository.existsRecipeById(recipeId)).isTrue();
    }

    @Test
    void testExistsRecipeByIdNotFound() {
        assertThat(recipeRepository.existsRecipeById(UUID.randomUUID())).isFalse();
    }

    @Test
    void testFindRecipeByName() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByName("Pancakes");

        assertThat(foundRecipe).isPresent().contains(recipe);
    }

    @Test
    void testFindRecipeByNameWhenNotExists() {
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByName("NonExistent Recipe");

        assertThat(foundRecipe).isEmpty();
    }

    @Test
    void testExistsRecipeByName() {
        recipeRepository.saveRecipe(recipe);
        assertThat(recipeRepository.existsRecipeByName(recipe.getRecipeName())).isTrue();
    }

    @Test
    void testExistsRecipeByNameNotFound() {
        assertThat(recipeRepository.existsRecipeByName("None")).isFalse();
    }

    @Test
    void testFindRecipeByCategoryContains() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByCategory(Category.DESSERT);

        assertTrue(foundRecipe.isPresent());
        assertEquals(Category.DESSERT, foundRecipe.get().getCategory());
    }

    @Test
    void testFindRecipeByCategoryContainsNotFound() {
        Category category =  Category.MAIN_DISH;
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByCategory(category);
        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByIngredientContains() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByIngredientContains("milk");

        assertTrue(foundRecipe.isPresent());
        assertTrue(foundRecipe.get().getIngredients().stream().anyMatch(ing -> ing.getProduct().getProductName().equalsIgnoreCase("milk")));
    }

    @Test
    void testFindRecipeByIngredientContainsNotFound() {
        String nonExistentIngredient = "vanilla";
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByIngredientContains(nonExistentIngredient);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByIngredientContainsNullIngredient() {
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByIngredientContains(null);

        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByProductId() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByProductId(recipe.getIngredients().getFirst().getProduct().getProductId());

        assertTrue(foundRecipe.isPresent());
        assertTrue(foundRecipe.get().getIngredients().stream().anyMatch(ing -> ing.getProduct().getProductId().equals(recipe.getIngredients().getFirst().getProduct().getProductId())));
    }

    @Test
    void testFindRecipeByProductIdNotFound() {
        Product nonExistentProduct = Product.newProduct(UUID.randomUUID(), "vanilla");

        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByProductName(nonExistentProduct.getProductName());
        assertFalse(foundRecipe.isPresent());
    }
    @Test
    void testFindRecipeByProductName() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByProductName("flour");

        assertTrue(foundRecipe.isPresent());
        assertTrue(foundRecipe.get().getIngredients().stream().anyMatch(ing -> ing.getProduct().getProductName().equalsIgnoreCase("flour")));
    }

    @Test
    void testFindRecipeByProductNameNotFound() {
        Product nonExistentProduct = Product.newProduct(UUID.randomUUID(), "vanilla");

        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByProductName(nonExistentProduct.getProductName());
        assertFalse(foundRecipe.isPresent());
    }

    @Test
    void testFindRecipeByCreatedAfter() {
        recipeRepository.saveRecipe(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findRecipeByCreatedAfter(Instant.now().minusSeconds(10));

        assertTrue(foundRecipe.isPresent());
    }

    @Test
    void testUpdateRecipe() {
        recipeRepository.saveRecipe(recipe);
        Recipe updatedRecipe = Recipe.newRecipe(recipe.getRecipeId(), "Pancakes - Updated",
                Category.DESSERT, recipe.getIngredients(),"New instructions", 10, List.of("Polish","Pancakes"));
        Recipe foundRecipe = recipeRepository.updateRecipe(updatedRecipe);

        assertThat(foundRecipe).isEqualTo(updatedRecipe);
        assertThat(recipeRepository.findRecipeById(recipeId)).contains(updatedRecipe);
    }

    @Test
    void testUpdateRecipeThrowsNotFoundException() {
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"flour"),200, MassUnit.G));
        Recipe newRecipe = Recipe.newRecipe(UUID.randomUUID(), "New Recipe",
                Category.DESSERT,ingredients,"New instructions", 10, List.of("Polish"));
        assertThatThrownBy(() -> recipeRepository.updateRecipe(newRecipe))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("Recipe with Id does not exist.");
    }

    @Test
    void testDeleteRecipeById() {
        recipeRepository.saveRecipe(recipe);
        recipeRepository.deleteRecipeById(recipeId);

        assertThat(recipeRepository.findRecipeById(recipeId)).isEmpty();
    }

    @Test
    void testDeleteRecipeByIdThrowsNotFoundException() {
        assertThatThrownBy(() -> recipeRepository.deleteRecipeById(UUID.randomUUID()))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("Recipe with Id does not exist.");
    }
}