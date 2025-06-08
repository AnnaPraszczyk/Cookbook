package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RecipeScalingResponse {
    private UUID recipeId;
    private String recipeName;
    private String category;
    private List<IngredientResponse> ingredients;
    private String instructions;
    private int servings;
    private List<String> tags;

    public static RecipeScalingResponse from(Recipe recipe) {
        return new RecipeScalingResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory().toString(),
                recipe.getIngredients().stream()
                        .map(ingredient ->
                                new IngredientResponse(
                                        ingredient.getProduct().getProductId(),
                                        ingredient.getProduct().getProductName(),
                                        ingredient.getAmount(),
                                        ingredient.getUnit()))
                        .collect(Collectors.toList()),
                recipe.getInstructions(),
                recipe.getNumberOfServings(),
                recipe.getTags());
    }
}


