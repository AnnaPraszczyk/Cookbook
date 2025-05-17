package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.CalculateServingsUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RecipeScalingService implements CalculateServingsUseCase, ScaleIngredientsUseCase {
   private final InMemoryRecipeRepository recipeRepository;

    @Override
    public int calculateServings(List<Ingredient> ingredients) {
        float totalMassInGrams = (float) ingredients.stream()
                .mapToDouble(ingredient -> ingredient.getUnit().toGrams(ingredient.getAmount()))
                .sum();
        return Math.max(1, Math.round(totalMassInGrams / 350));
    }

    @Override
    public Recipe adjustRecipeByServings(AdjustRecipeRequest request) {
        Recipe selectedRecipe = findRecipe(request.recipeName(), request.choice());
        return selectedRecipe != null ? scaleRecipe(selectedRecipe, request.servings()) : null;
    }


    private Recipe findRecipe(String recipeName, int choice) {
        List<Recipe> matchingRecipes = recipeRepository.findRecipeByName(recipeName);
        return matchingRecipes.isEmpty() ? null :
                (choice > 0 && choice <= matchingRecipes.size()) ? matchingRecipes.get(choice - 1) : matchingRecipes.getFirst();
    }

    private Recipe scaleRecipe(Recipe recipe, int servings) {
        List<Ingredient> adjustedIngredients = recipe.getIngredients().stream()
                .map(ingredient -> Ingredient.newIngredient(
                        Product.newProduct(ingredient.getProduct().getProductId(), ingredient.getProduct().getProductName()),
                        ingredient.getAmount() * servings / recipe.getNumberOfServings(),
                        ingredient.getUnit()))
                .toList();

        return Recipe.newRecipe(UUID.randomUUID(),
                recipe.getRecipeName() + " (" + servings + " servings)",
                recipe.getCategory(),
                adjustedIngredients,
                recipe.getInstructions(),
                servings,
                recipe.getTags());
    }
}