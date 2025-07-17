package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import java.util.List;

public record RecipeRequest(String recipeName, Category category, List<IngredientRequest> ingredients, String instructions,
                            int numberOfServings, List<String> tags) {}

