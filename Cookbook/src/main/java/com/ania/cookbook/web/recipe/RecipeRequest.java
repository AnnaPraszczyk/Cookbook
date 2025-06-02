package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;

import java.util.List;

public record RecipeRequest(String recipeName, Category category, List<Ingredient> ingredients, String instructions,
                            int numberOfServings, List<String> tags) {}
