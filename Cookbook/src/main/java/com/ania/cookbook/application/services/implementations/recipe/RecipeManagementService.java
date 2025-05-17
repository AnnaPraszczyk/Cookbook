package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeManagementService implements ListManagementUseCase {
    private final InMemoryRecipeRepository recipeRepository;
    private final List<Recipe> recipeList = new ArrayList<>();

    @Override
    public List<Recipe> findRecipesByName(String name) {
        return recipeRepository.findRecipeByName(name);
    }

    @Override
    public boolean addRecipeByChoice(int choice, List<Recipe> matchingRecipes) {
        if (choice > 0 && choice <= matchingRecipes.size()) {
            recipeList.add(matchingRecipes.get(choice - 1));
            return true;
        }
        return false;
    }

    @Override
    public List<Recipe> getRecipeList() {
        return new ArrayList<>(recipeList);
    }

    @Override
    public void saveRecipeList() {
        recipeList.forEach(recipeRepository::saveRecipe);
    }

    @Override
    public boolean removeRecipeFromList(String recipeName, int choice) {
        List<Recipe> matchingRecipes = recipeRepository.findRecipeByName(recipeName);
        if (!matchingRecipes.isEmpty() && choice > 0 && choice <= matchingRecipes.size()) {
            recipeList.remove(matchingRecipes.get(choice - 1));
            return true;
        }
        return false;
    }

    @Override
    public boolean clearRecipeList(boolean confirm) {
        if (!recipeList.isEmpty() && confirm) {
            recipeList.clear();
            return true;
        }
        return false;
    }
}
