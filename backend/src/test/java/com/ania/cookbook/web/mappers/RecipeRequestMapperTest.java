package com.ania.cookbook.web.mappers;
import com.ania.cookbook.application.services.implementations.recipe.CreateRecipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeRequestMapperTest {
    private RecipeRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecipeRequestMapper();
    }

    @Test
    void mapRecipeRequestToCreateRecipeCorrectly() {
        RecipeRequest request = new RecipeRequest(
                "Lasagna",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                "Bake layers",
                4,
                List.of("italian", "dinner"));
        CreateRecipe result = mapper.toCreateRecipe(request);

        assertEquals("Lasagna", result.getRecipeName());
        assertEquals(Category.MAIN_COURSE, result.getCategory());
        assertEquals(1, result.getIngredients().size());
        assertEquals("Sugar", result.getIngredients().getFirst().productName());
        assertEquals("Bake layers", result.getInstructions());
        assertEquals(4, result.getNumberOfServings());
        assertEquals(List.of("italian", "dinner"), result.getTags());
    }

    @Test
    void handleNullIngredientsAndTagsGracefully() {
        RecipeRequest request = new RecipeRequest(
                "Soup",
                Category.MAIN_COURSE,
                null,
                "Boil water",
                2,
                null);
        CreateRecipe result = mapper.toCreateRecipe(request);

        assertEquals("Soup", result.getRecipeName());
        assertEquals(Category.MAIN_COURSE, result.getCategory());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
        assertEquals("Boil water", result.getInstructions());
        assertEquals(2, result.getNumberOfServings());
        assertNotNull(result.getTags());
        assertTrue(result.getTags().isEmpty());
    }
}