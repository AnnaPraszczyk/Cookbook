package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConversionServiceTest {

    private ConversionService conversionService;

    @BeforeEach
    void setUp() {
        Converter<ProductEntity, Product> productConverter = new ProductConverter();
        Converter<Product, ProductEntity> productEntityConverter = new ProductEntityConverter();
        Converter<RecipeEntity, Recipe> recipeConverter = new RecipeConverter();
        Converter<Recipe, RecipeEntity> recipeEntityConverter = new RecipeEntityConverter();
        conversionService = new ConversionService(productConverter, productEntityConverter, recipeConverter, recipeEntityConverter);
    }
    @Test
    void ConvertProductEntityToDomain() {
        ProductEntity entity = ProductEntity.newProductEntity(UUID.randomUUID(), "Flour");
        Product product = conversionService.convertProductToDomain(entity);

        assertNotNull(product);
        assertEquals(entity.getProductId(), product.getProductId());
        assertEquals(entity.getProductName(), product.getProductName());
    }

    @Test
    void ConvertProductToEntity() {
        Product product = Product.newProduct(UUID.randomUUID(), "Flour");
        ProductEntity entity = conversionService.convertProductToEntity(product);

        assertNotNull(entity);
        assertEquals(product.getProductId(), entity.getProductId());
        assertEquals(product.getProductName(), entity.getProductName());
    }

    @Test
    void ConvertRecipeEntityToDomain() {
        String jsonIngredients = "[{\"product\":{\"productId\":\"550e8400-e29b-41d4-a716-446655440000\",\"productName\":\"Flour\"},\"amount\":10,\"unit\":\"DAG\"}]";
        RecipeEntity entity = RecipeEntity.newRecipeEntity(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                jsonIngredients,"Mix and cook", 2, List.of("Easy"));
        Recipe recipe = conversionService.convertRecipeToDomain(entity);

        assertNotNull(recipe);
        assertEquals(entity.getRecipeId(), recipe.getRecipeId());
        assertEquals(entity.getRecipeName(), recipe.getRecipeName());
        assertEquals(entity.getInstructions(), recipe.getInstructions());
    }

    @Test
    void ConvertRecipeToEntity() {
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pancakes", Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),"Flour"), 10f, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        RecipeEntity entity = conversionService.convertToEntity(recipe);

        assertNotNull(entity);
        assertEquals(recipe.getRecipeId(), entity.getRecipeId());
        assertEquals(recipe.getRecipeName(), entity.getRecipeName());
        assertEquals(recipe.getInstructions(), entity.getInstructions());
    }
}

