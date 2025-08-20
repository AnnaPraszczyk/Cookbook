package com.ania.cookbook.application.services.interfaces.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface UpdateRecipeUseCase {
    Recipe updateRecipe(UUID recipeId, UpdateRecipeCase recipe);
    record UpdateRecipeCase(
            @NotBlank String name,
            @NotNull Category category,
            @NotEmpty @Valid List<IngredientRequest> ingredients,
            @NotBlank String instructions,
            @Min(0) int numberOfServings,
            List<String> tags){}
}

