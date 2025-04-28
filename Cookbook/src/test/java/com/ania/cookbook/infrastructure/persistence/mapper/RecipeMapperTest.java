package com.ania.cookbook.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class RecipeMapperTest {

    @Test
    void testToDomainValidData() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.DESSERT, Category.VEGAN);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(null, ProductEntity.newProductEntity(UUID.randomUUID(), "Sugar"),12f, Unit.KG,null));
        String instructions = "Mix everything and bake";
        int numberOfServings = 4;

        RecipeEntity entity = RecipeEntity.newRecipeEntity(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        Recipe domain = RecipeMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(recipeId, domain.getRecipeId());
        assertEquals(recipeName, domain.getRecipeName());
        assertEquals(categories, domain.getCategories());
        assertEquals(ingredients, domain.getIngredients());
        assertEquals(instructions, domain.getInstructions());
        assertEquals(numberOfServings, domain.getNumberOfServings());
    }

    @Test
    void testToDomainNullEntity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RecipeMapper.toDomain(null));

        assertEquals("RecipeEntity cannot be null", exception.getMessage());
    }

    @Test
    void testToEntityValidData() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Test Recipe";
        List<Category> categories = List.of(Category.MAIN_DISH);
        List<Ingredient> ingredients = List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), "Flour"), null,300f, Unit.G,null));
        String instructions = "Mix and fry";
        int numberOfServings = 3;

        Recipe domain = Recipe.newRecipe(recipeId, recipeName, categories, ingredients, instructions, numberOfServings);

        RecipeEntity entity = RecipeMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(recipeId, entity.getRecipeId());
        assertEquals(recipeName, entity.getRecipeName());
        assertEquals(categories, entity.getCategories());
        assertEquals(ingredients, entity.getIngredients());
        assertEquals(instructions, entity.getInstructions());
        assertEquals(numberOfServings, entity.getNumberOfServings());
    }

    @Test
    void testToEntityNullDomain() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> RecipeMapper.toEntity(null));

        assertEquals("Recipe cannot be null", exception.getMessage());
    }

}