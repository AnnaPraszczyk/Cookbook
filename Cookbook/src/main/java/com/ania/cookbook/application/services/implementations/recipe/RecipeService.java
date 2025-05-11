package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.RecipeRequest;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class RecipeService implements CreateRecipeUseCase, UpdateRecipeUseCase, DeleteRecipeUseCase {
    private final SaveRecipe saveRecipeRepository;
    private final ReadRecipe readRecipeRepository;
    private final UpdateRecipe updateRecipeRepository;
    private final DeleteRecipe deleteRecipeRepository;

    @Override
    public Recipe createRecipe(CreateRecipeRequest request) {
        validateRecipeRequest(request);
        var newRecipe = Recipe.newRecipe(UUID.randomUUID(),request.recipeName(),request.category(),
                request.ingredients() != null ? List.copyOf(request.ingredients()) : List.of(),
                request.instructions(),request.numberOfServings(),
                request.tags() != null ? List.copyOf(request.tags()) : List.of());
        return saveRecipeRepository.saveRecipe(newRecipe);
    }

    @Override
    public Recipe updateRecipe(UpdateRecipeRequest request) {
        validateRecipeRequest(request);
        List<Recipe> matchingRecipes = readRecipeRepository.findRecipeByName(request.recipeName());
        if (matchingRecipes.isEmpty()) {
            throw new RecipeNotFoundException("Recipe with given name does not exist.");
        }
        Recipe recipeToUpdate;
        if(matchingRecipes.size()>1) {
            recipeToUpdate = selectRecipeFromList(matchingRecipes);
        } else recipeToUpdate = matchingRecipes.getFirst();
        Recipe updatedRecipe = Recipe.newRecipe(recipeToUpdate.getRecipeId(),
                request.recipeName(), request.category(),
                request.ingredients() != null ? List.copyOf(request.ingredients()) : List.of(),
                request.instructions(),request.numberOfServings(),
                request.tags() != null ? List.copyOf(request.tags()) : List.of());
        return updateRecipeRepository.updateRecipe(updatedRecipe);
    }

    private Recipe selectRecipeFromList(List<Recipe> recipes) {
        return recipes.getFirst();
    }

    @Override
    public void deleteRecipe(DeleteRecipeRequest request) {
        if (isBlank(request.recipeName()))
            throw new RecipeValidationException("Recipe cannot be null");
        List<Recipe> matchingRecipes = readRecipeRepository.findRecipeByName(request.recipeName());
        if (matchingRecipes.isEmpty()) {
            throw new RecipeNotFoundException("Recipe with given name does not exist.");
        }
        Recipe recipeToDelete = (matchingRecipes.size()>1) ?
            selectRecipeFromList(matchingRecipes) :
                matchingRecipes.getFirst();
        deleteRecipeRepository.deleteRecipeById(recipeToDelete.getRecipeId());
    }

    public void validateRecipeRequest(RecipeRequest request) {
        if (request == null) {
            throw new RecipeValidationException("Recipe cannot be null");
        }

        if (StringUtils.isBlank(request.recipeName())) {
            throw new RecipeValidationException("Recipe name cannot be null or empty.");
        }

        if (request.category() == null) {
            throw new RecipeValidationException("Recipe category cannot be null.");
        }

        if (request.ingredients() == null || request.ingredients().isEmpty()) {
            throw new RecipeValidationException("Recipe ingredients cannot be null or empty.");
        }

        if (StringUtils.isBlank(request.instructions())) {
            throw new RecipeValidationException("Recipe instructions cannot be null or empty.");
        }

        if (request.numberOfServings() < 0) {
            throw new RecipeValidationException("Recipe number of servings cannot be negative.");
        }
    }
}

