package com.ania.cookbook.web.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import lombok.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadRecipeResponse {
    private UUID id;
    private String name;
    private Category category;
    private List<IngredientResponse> ingredients;
    private String instructions;
    private int numberOfServings;
    private List<String> tags;

    public static ReadRecipeResponse from(Recipe recipe) {
        List<IngredientResponse> ingredientResponses = recipe.getIngredients() != null
                ? recipe.getIngredients().stream()
                .map(i -> new IngredientResponse(
                        i.getProduct().getProductId(),
                        i.getProduct().getProductName(),
                        i.getAmount(),
                        i.getUnit()))
                .toList()
                : List.of();
        return ReadRecipeResponse.builder()
                .id(recipe.getRecipeId())
                .name(recipe.getRecipeName())
                .category(recipe.getCategory())
                .ingredients(ingredientResponses)
                .instructions(Optional.ofNullable(recipe.getInstructions()).orElse(""))
                .numberOfServings(recipe.getNumberOfServings())
                .tags(recipe.getTags() != null ? recipe.getTags() : List.of())
                .build();
    }
}
