package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.implementations.recipe.RecipeScalingService;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.ania.cookbook.web.recipe.RecipeScalingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeScalingControllerTest {
    @Mock
    private RecipeScalingService recipeScalingService;

    @Mock
    private RecipeResponseMapper recipeResponseMapper;

    @InjectMocks
    private RecipeScalingController controller;

    @Test
    void scaleRecipe() {
        UUID recipeId = UUID.randomUUID();
        int newServings = 4;
        RecipeScalingRequest request = RecipeScalingRequest.builder()
                .recipeId(recipeId)
                .servings(newServings)
                .build();
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Flour"));
        Ingredient ingredient = Ingredient.newIngredient(product, 200.0f, Unit.G);
        Recipe scaledRecipe = Recipe.newRecipe(
                recipeId,
                "Test Recipe (4 servings)",
                Category.MAIN_COURSE,
                List.of(ingredient),
                "Mix ingredients",
                newServings,
                List.of("tag1", "tag2"));
        RecipeResponse mappedResponse = new RecipeResponse(
                scaledRecipe.getRecipeId(),
                scaledRecipe.getRecipeName(),
                scaledRecipe.getCategory(),
                List.of(new IngredientResponse(
                        product.getProductId(),
                        product.getProductName(),
                        ingredient.getAmount(),
                        ingredient.getUnit()
                )),
                scaledRecipe.getInstructions(),
                scaledRecipe.getNumberOfServings(),
                scaledRecipe.getTags());
        when(recipeScalingService.adjustRecipeByServings(any(AdjustRecipe.class)))
                .thenReturn(scaledRecipe);
        when(recipeResponseMapper.toResponse(scaledRecipe)).thenReturn(mappedResponse);
        ResponseEntity<RecipeResponse> response = controller.scaleRecipe(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mappedResponse, response.getBody());
    }

    @Test
    void scaleRecipe_shouldReturnBadRequest_whenServingsIsNegative() {
        RecipeScalingRequest request = RecipeScalingRequest.builder()
                .recipeId(UUID.randomUUID())
                .servings(-1)
                .build();

        assertThrows(RecipeValidationException.class, () -> controller.scaleRecipe(request));
    }

    @Test
    void scaleRecipe_shouldThrowException_whenRecipeIdIsNull() {
        RecipeScalingRequest request = RecipeScalingRequest.builder()
                .recipeId(null)
                .servings(2)
                .build();

        assertThrows(RecipeNotFoundException.class, () -> controller.scaleRecipe(request));
    }

    @Test
    void scaleRecipe_shouldPropagateException_whenServiceFails() {
        RecipeScalingRequest request = RecipeScalingRequest.builder()
                .recipeId(UUID.randomUUID())
                .servings(2)
                .build();

        when(recipeScalingService.adjustRecipeByServings(any())).thenThrow(new RecipeValidationException("Invalid scaling"));

        assertThrows(RecipeValidationException.class, () -> controller.scaleRecipe(request));
    }
}