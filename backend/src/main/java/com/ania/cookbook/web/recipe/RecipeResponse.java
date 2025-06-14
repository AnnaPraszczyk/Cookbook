package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;

import java.util.List;
import java.util.UUID;

public record RecipeResponse(UUID recipeId, String recipeName, Category category, List<Ingredient> ingredients, String instructions,
                             int numberOfServings, List<String> tags) {}
