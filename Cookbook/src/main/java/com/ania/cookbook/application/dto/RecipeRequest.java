package com.ania.cookbook.application.dto;

import com.ania.cookbook.domain.model.Ingredient;

public record RecipeRequest(String name, String category, Ingredient ingredients, String description, int numberOfServings) {

}
