package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipe;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createRecipe() {
        UUID recipeId = UUID.randomUUID();

        RecipeRequest request = new RecipeRequest(
                "Lasagna",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                "Bake layers",
                4,
                List.of("italian")
        );
        Ingredient domainIngredient = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), ProductName.from("Sugar")),
                200, Unit.G
        );

        Recipe recipe = Recipe.newRecipe(
                recipeId,
                request.recipeName(),
                request.category(),
                List.of(domainIngredient),
                request.instructions(),
                request.numberOfServings(),
                request.tags()
        );
        when(recipeService.createRecipe(any(CreateRecipe.class))).thenReturn(recipe);
        ResponseEntity<RecipeResponse> response = recipeController.createRecipe(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Lasagna", response.getBody().recipeName());
        assertEquals(Category.MAIN_COURSE, response.getBody().category());
        assertEquals(4, response.getBody().numberOfServings());
        }

    @Test
    void shouldUpdateRecipe() {
        UUID recipeId = UUID.randomUUID();
        Ingredient ingredient = Ingredient.newIngredient(
                Product.newProduct(UUID.randomUUID(), ProductName.from("Sugar")),
                200,
                Unit.G
        );
        Recipe updated = Recipe.newRecipe(
                recipeId,
                "Pizza",
                Category.MAIN_COURSE,
                List.of(ingredient),
                "Bake",
                2,
                List.of("cheese")
        );
        when(recipeService.updateRecipe(eq(recipeId), any(UpdateRecipeCase.class))).thenReturn(updated);
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                updated.getRecipeName(),
                updated.getCategory(),
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                updated.getInstructions(),
                updated.getNumberOfServings(),
                updated.getTags()
        );
        ResponseEntity<RecipeResponse> response = recipeController.updateRecipe(recipeId, updateRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Pizza", response.getBody().recipeName());
        assertEquals(2, response.getBody().numberOfServings());
        assertEquals(1, response.getBody().ingredients().size());
        assertEquals("Sugar", response.getBody().ingredients().getFirst().getProduct().getProductName().getName());
    }

    @Test
    void shouldDeleteRecipe() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Pizza";
        DeleteRecipeCase deleteCase = new DeleteRecipeCase(recipeId, recipeName);

        doNothing().when(recipeService).deleteRecipe(deleteCase);

        ResponseEntity<Void> response = recipeController.deleteRecipe(recipeId, deleteCase);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(recipeService, times(1)).deleteRecipe(deleteCase);
    }


}