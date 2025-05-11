package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;

import java.util.List;

public interface RecipeRequest {
    String recipeName();
    Category category();
    List<Ingredient> ingredients();
    String instructions();
    int numberOfServings();
    List<String> tags();
}
