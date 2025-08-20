package com.ania.cookbook.web.mappers;
import com.ania.cookbook.application.services.implementations.recipe.CreateRecipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
public class RecipeRequestMapper {
    public CreateRecipe toCreateRecipe(RecipeRequest request) {
        List<IngredientRequest> ingredientRequests = request.ingredients() != null
                ? request.ingredients()
                : Collections.emptyList();
        return CreateRecipe.builder()
                .recipeName(request.recipeName())
                .category(request.category())
                .ingredients(ingredientRequests)
                .instructions(request.instructions())
                .numberOfServings(request.numberOfServings())
                .tags(request.tags() != null ? request.tags() : Collections.emptyList())
                .build();
    }
}
