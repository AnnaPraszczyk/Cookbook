package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.recipe.CreateRecipe;
import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.mappers.RecipeRequestMapper;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private RecipeRequestMapper recipeRequestMapper;

    @Mock
    private RecipeResponseMapper recipeResponseMapper;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    void createRecipe() {
        UUID recipeId = UUID.randomUUID();
        RecipeRequest request = new RecipeRequest(
                "Lasagna",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                "Bake layers",
                4,
                List.of("italian"));
        CreateRecipe createRecipe = CreateRecipe.builder()
                .recipeName("Lasagna")
                .category(Category.MAIN_COURSE)
                .ingredients(request.ingredients())
                .instructions("Bake layers")
                .numberOfServings(4)
                .tags(List.of("italian"))
                .build();
        Recipe recipe = Recipe.newRecipe(
                recipeId,
                request.recipeName(),
                request.category(),
                List.of(),
                request.instructions(),
                request.numberOfServings(),
                request.tags());
        RecipeResponse responseDto = new RecipeResponse(
                recipeId,
                "Lasagna",
                Category.MAIN_COURSE,
                List.of(),
                "Bake layers",
                4,
                List.of("italian"));
        when(recipeRequestMapper.toCreateRecipe(request)).thenReturn(createRecipe);
        when(recipeService.createRecipe(createRecipe)).thenReturn(recipe);
        when(recipeResponseMapper.toResponse(recipe)).thenReturn(responseDto);
        ResponseEntity<RecipeResponse> response = recipeController.createRecipe(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Lasagna", response.getBody().recipeName());
        assertEquals(Category.MAIN_COURSE, response.getBody().category());
        assertEquals(4, response.getBody().numberOfServings());
        assertEquals(List.of("italian"), response.getBody().tags());
    }

    @Test
    void updateRecipe() {
        UUID recipeId = UUID.randomUUID();
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Pizza",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                "Bake",
                2,
                List.of("cheese"));
        Recipe updatedRecipe = Recipe.newRecipe(
                recipeId,
                updateRequest.name(),
                updateRequest.category(),
                List.of(),
                updateRequest.instructions(),
                updateRequest.numberOfServings(),
                updateRequest.tags());
        RecipeResponse responseDto = new RecipeResponse(
                recipeId,
                "Pizza",
                Category.MAIN_COURSE,
                List.of(),
                "Bake",
                2,
                List.of("cheese"));

        when(recipeService.updateRecipe(eq(recipeId), eq(updateRequest))).thenReturn(updatedRecipe);
        when(recipeResponseMapper.toResponse(updatedRecipe)).thenReturn(responseDto);
        ResponseEntity<RecipeResponse> response = recipeController.updateRecipe(recipeId, updateRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pizza", response.getBody().recipeName());
        assertEquals(2, response.getBody().numberOfServings());
        assertEquals(List.of("cheese"), response.getBody().tags());
    }

    @Test
    void deleteRecipe() {
        UUID recipeId = UUID.randomUUID();
        String recipeName = "Pizza";
        DeleteRecipeCase deleteCase = new DeleteRecipeCase(recipeId, recipeName);

        doNothing().when(recipeService).deleteRecipe(deleteCase);
        ResponseEntity<Void> response = recipeController.deleteRecipe(recipeId, deleteCase);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(recipeService, times(1)).deleteRecipe(deleteCase);
    }

    @Test
    void throwExceptionWhenPathIdAndBodyIdMismatch() {
        UUID pathId = UUID.randomUUID();
        UUID bodyId = UUID.randomUUID();
        DeleteRecipeCase deleteCase = new DeleteRecipeCase(bodyId, "Pizza");

        assertThrows(RecipeValidationException.class, () -> recipeController.deleteRecipe(pathId, deleteCase));
    }

    @Test
    void returnErrorWhenRecipeNotFound() {
        UUID recipeId = UUID.randomUUID();
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Pizza",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 200, Unit.G)),
                "Bake",
                2,
                List.of("cheese"));
        when(recipeService.updateRecipe(eq(recipeId), eq(updateRequest)))
                .thenThrow(new RecipeNotFoundException("Recipe not found"));

        assertThrows(RecipeNotFoundException.class, () -> recipeController.updateRecipe(recipeId, updateRequest));
    }
}