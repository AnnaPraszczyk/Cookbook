package com.ania.cookbook.web.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record RecipeResponse(UUID recipeId, String recipeName, Category category, List<IngredientResponse> ingredients, String instructions,
                             int numberOfServings, List<String> tags) {
    public static RecipeResponse from(Recipe recipe) {
        List<IngredientResponse> ingredientResponses = recipe.getIngredients() != null
                ? recipe.getIngredients().stream()
                .map(i -> new IngredientResponse(
                        i.getProduct().getProductId(),
                        i.getProduct().getProductName(),
                        i.getAmount(),
                        i.getUnit()))
                .toList()
                : List.of();

        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory(),
                ingredientResponses,
                Optional.ofNullable(recipe.getInstructions()).orElse(""),
                recipe.getNumberOfServings(),
                recipe.getTags() != null ? recipe.getTags() : List.of()
        );
    }
}
