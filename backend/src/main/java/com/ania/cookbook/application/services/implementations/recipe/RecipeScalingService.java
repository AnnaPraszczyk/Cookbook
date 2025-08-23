package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RecipeScalingService implements ScaleIngredientsUseCase {
    private final ReadRecipe readRecipeRepository;

    @Override
    public Recipe adjustRecipeByServings(AdjustRecipe recipe) {
        Recipe selectedRecipe = readRecipeRepository.findRecipeById(recipe.recipeId())
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with given Id does not exist."));

        List<Ingredient> adjustedIngredients = recipe.servings() > 0
        ? selectedRecipe.getIngredients().stream()
                .map(ingredient -> Ingredient.newIngredient(
                        ingredient.getProduct(),
                        Math.round(ingredient.getAmount() * recipe.servings() / selectedRecipe.getNumberOfServings()),
                        ingredient.getUnit()))
                .toList()
                : selectedRecipe.getIngredients();

        return Recipe.newRecipe(UUID.randomUUID(),
                selectedRecipe.getRecipeName(),
                selectedRecipe.getCategory(),
                adjustedIngredients,
                selectedRecipe.getInstructions(),
                recipe.servings(),
                selectedRecipe.getTags());
    }
}