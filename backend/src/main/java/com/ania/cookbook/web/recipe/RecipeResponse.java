package com.ania.cookbook.web.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import java.util.List;
import java.util.UUID;

public record RecipeResponse(UUID recipeId, String recipeName, Category category, List<IngredientResponse> ingredients, String instructions,
                             int numberOfServings, List<String> tags) {}
