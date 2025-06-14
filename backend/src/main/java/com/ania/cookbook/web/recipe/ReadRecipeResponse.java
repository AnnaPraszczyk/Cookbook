package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadRecipeResponse {
    private UUID id;
    private String name;
    private Category category;
    private List<Ingredient> ingredients;
    private String instructions;
    private int numberOfServings;
    private List<String> tags;

    public static ReadRecipeResponse from(Recipe recipe) {
        return ReadRecipeResponse.builder()
                .id(recipe.getRecipeId())
                .name(recipe.getRecipeName())
                .category(recipe.getCategory())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .tags(recipe.getTags())
                .build();
    }

}
