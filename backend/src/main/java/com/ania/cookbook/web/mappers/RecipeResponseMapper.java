package com.ania.cookbook.web.mappers;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import com.ania.cookbook.web.recipe.RecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeResponseMapper {
    private final IngredientMapper ingredientMapper;

    public RecipeResponse toResponse(Recipe recipe) {
        List<IngredientResponse> ingredients = recipe.getIngredients().stream()
                .map(ingredientMapper::toResponse)
                .toList();
        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory(),
                ingredients,
                recipe.getInstructions(),
                recipe.getNumberOfServings(),
                recipe.getTags()
        );
    }

    public List<RecipeResponse> toResponseList(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<RecipeResponse> toResponsePage(Page<Recipe> recipePage) {
        return recipePage.map(this::toResponse);
    }

}
