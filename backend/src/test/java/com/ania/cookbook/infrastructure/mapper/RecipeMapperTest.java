package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeMapperTest {
    private RecipeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecipeMapper();
    }

    @Test
    void toDomain() {
        UUID recipeId = UUID.randomUUID();
        IngredientJson ingJson = new IngredientJson(UUID.randomUUID(), "Sugar", 100.0f, Unit.G);
        RecipeEntity entity = RecipeEntity.newRecipeEntity(recipeId, "Cake", Category.DESSERT, List.of(ingJson),
                "Mix and bake for 40 minutes.", 4, List.of("fast", "sweet"));
        Recipe domain = mapper.toDomain(entity);

        assertEquals(recipeId, domain.getRecipeId());
        assertEquals("Cake", domain.getRecipeName());
        assertEquals(Category.DESSERT, domain.getCategory());
        assertEquals(4, domain.getNumberOfServings());
        assertEquals("Mix and bake for 40 minutes.", domain.getInstructions());
        assertEquals(List.of("fast", "sweet"), domain.getTags());
        assertEquals(1, domain.getIngredients().size());
        Ingredient ing = domain.getIngredients().getFirst();
        assertEquals("Sugar", ing.getProduct().getProductName().name());
        assertEquals(100.0f, ing.getAmount());
        assertEquals(Unit.G, ing.getUnit());
    }

    @Test
    void toEntity() {
        UUID recipeId = UUID.randomUUID();
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Butter"));
        Ingredient ing = Ingredient.newIngredient(product, 200f, Unit.G);
        Recipe domain = Recipe.newRecipe(recipeId, "Pancakes", Category.DESSERT, List.of(ing), "Fry on butter.",
                2, List.of("easy", "fast"));
        RecipeEntity entity = mapper.toEntity(domain);

        assertEquals(recipeId, entity.getRecipeId());
        assertEquals("Pancakes", entity.getRecipeName());
        assertEquals(Category.DESSERT, entity.getCategory());
        assertEquals("Fry on butter.", entity.getInstructions());
        assertEquals(2, entity.getNumberOfServings());
        assertEquals(List.of("easy", "fast"), entity.getTags());
        assertEquals(1, entity.getIngredients().size());
        IngredientJson ingJson = entity.getIngredients().getFirst();
        assertEquals("Butter", ingJson.getProductName());
        assertEquals(200f, ingJson.getAmount());
        assertEquals(Unit.G, ingJson.getUnit());
    }

    @Test
    void toDomainWithEmptyIngredients() {
        RecipeEntity entity = RecipeEntity.newRecipeEntity(UUID.randomUUID(), "Test", Category.MAIN_COURSE, List.of(),
                "Instructions", 1, List.of());
        Recipe result = mapper.toDomain(entity);

        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void toEntity_withEmptyIngredients_shouldReturnEmptyList() {
        Recipe domain = Recipe.newRecipe(UUID.randomUUID(), "Test", Category.APPETIZER, List.of(), "Instructions",
                1, List.of());
        RecipeEntity result = mapper.toEntity(domain);

        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }
}