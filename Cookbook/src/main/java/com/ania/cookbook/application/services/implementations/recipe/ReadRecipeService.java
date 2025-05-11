package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadRecipeService implements FindRecipeUseCase{
    private final ReadRecipe readRecipeRepository;

    @Override
    public Optional<Recipe> findRecipeById(FindRecipeById recipe) {
        return Optional.ofNullable(readRecipeRepository.findRecipeById(recipe.id()).
                orElseThrow(() -> new RecipeNotFoundException("Unable to find the recipe because it does not exist.")));
    }

    @Override
    public boolean existsRecipeById(FindRecipeById recipe){
        if(isBlank(recipe.id().toString())) {
            throw new RecipeValidationException("Recipe id cannot be null or empty.");
        }
        return readRecipeRepository.existsRecipeById(recipe.id());
    }

    @Override
    public List<Recipe> findRecipeByName(FindRecipeByName recipe) {
        if(isBlank(recipe.name())) {
            throw new RecipeValidationException("Recipe name cannot be null or empty.");
        }
        List<Recipe> matchingRecipes = readRecipeRepository.findRecipeByName(recipe.name());
        if (matchingRecipes.isEmpty()) {
            throw new RecipeNotFoundException("Recipe with given name does not exist.");
        }

        return matchingRecipes.size()>1 ? List.copyOf(matchingRecipes) : List.of(selectRecipeFromList(matchingRecipes));
    }
    private Recipe selectRecipeFromList(List<Recipe> recipes) {
        return recipes.getFirst();
    }

    @Override
    public boolean existsRecipeByName(FindRecipeByName recipe){
        if(isBlank(recipe.name())) {
            throw new RecipeValidationException("Recipe name cannot be null or empty.");
        }
        return readRecipeRepository.existsRecipeByName(recipe.name());
    }

    @Override
    public List<Recipe> findRecipeByCategory(FindRecipeByCategory recipe) {
        if(recipe.category() == null) {
            throw new RecipeValidationException("Recipe category cannot be null.");
        }
        List<Recipe> recipes = readRecipeRepository.findRecipeByCategory(recipe.category());
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for the given category.");
        }
        return recipes.size()>1 ? List.copyOf(recipes) : List.of(selectRecipeFromList(recipes));
    }

    @Override
    public List<Recipe> findRecipeByTag(FindRecipeByTag recipe) {
        if(isBlank(recipe.tag())) {
            throw new RecipeValidationException("Recipe tag cannot be null or empty.");
        }
        List<Recipe> recipes = readRecipeRepository.findRecipeByTag(recipe.tag());
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for the given tag.");
        }
        return recipes.size()>1 ? List.copyOf(recipes) : List.of(selectRecipeFromList(recipes));
    }
}
