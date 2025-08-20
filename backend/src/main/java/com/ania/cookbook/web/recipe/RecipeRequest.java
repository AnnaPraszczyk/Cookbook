package com.ania.cookbook.web.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RecipeRequest(
        @NotBlank String recipeName,
        @NotNull Category category,
        @NotEmpty @Valid List<IngredientRequest> ingredients,
        @NotBlank String  instructions,
        @Min(0) int numberOfServings,
        List<String> tags) {}

