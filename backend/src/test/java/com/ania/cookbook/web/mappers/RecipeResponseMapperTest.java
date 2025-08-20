package com.ania.cookbook.web.mappers;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import com.ania.cookbook.web.recipe.RecipeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeResponseMapperTest {
    @Mock
    private IngredientMapper ingredientMapper;

    private RecipeResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecipeResponseMapper(ingredientMapper);}

    @Test
    void shouldMapRecipeToResponseCorrectly() {
        UUID productId = UUID.randomUUID();
        ProductName productName = ProductName.from("Sugar");
        Product product = Product.newProduct(productId, productName);
        Ingredient ingredient = Ingredient.newIngredient(product, 200, Unit.G);
        IngredientResponse ingredientResponse = new IngredientResponse(productId, productName, 200, Unit.G);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(
                recipeId,
                "Lasagna",
                Category.MAIN_COURSE,
                List.of(ingredient),
                "Bake layers",
                4,
                List.of("italian")
        );
        when(ingredientMapper.toResponse(ingredient)).thenReturn(ingredientResponse);
        RecipeResponse response = mapper.toResponse(recipe);

        assertEquals(recipeId, response.recipeId());
        assertEquals("Lasagna", response.recipeName());
        assertEquals(Category.MAIN_COURSE, response.category());
        assertEquals("Bake layers", response.instructions());
        assertEquals(4, response.numberOfServings());
        assertEquals(List.of("italian"), response.tags());
        assertEquals(1, response.ingredients().size());
        assertEquals(ingredientResponse, response.ingredients().getFirst());
    }

    @Test
    void shouldMapRecipeWithEmptyIngredients() {
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(
                recipeId,
                "Pancakes",
                Category.DESSERT,
                List.of(),
                "Fry",
                1,
                List.of("simple")
        );
        RecipeResponse response = mapper.toResponse(recipe);

        assertEquals(recipeId, response.recipeId());
        assertEquals("Pancakes", response.recipeName());
        assertEquals(Category.DESSERT, response.category());
        assertEquals("Fry", response.instructions());
        assertEquals(1, response.numberOfServings());
        assertEquals(List.of("simple"), response.tags());
        assertTrue(response.ingredients().isEmpty());
    }
}