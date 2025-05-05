package com.ania.cookbook.infrastructure.dto;

import com.ania.cookbook.application.services.CreateRecipe;
import com.ania.cookbook.domain.model.Ingredient;

import java.util.Collections;

public record CreateRecipeRequest(
        String name,
        String category,
        Ingredient ingredients,
        String description,
        int numberOfServings
) {

    public CreateRecipe toCommand() {
        return new CreateRecipe("34b00ac8-4fde-45a9-850e-7c231e5b719d", Collections.emptyList());
    }
}
