package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Recipe;
import java.util.List;

public interface ListManagementUseCase {

    List<Recipe> findRecipesByName(String name);
    boolean addRecipeByChoice(int choice, List<Recipe> matchingRecipes);
    List<Recipe> getRecipeList();
    void saveRecipeList();
    boolean removeRecipeFromList(String recipeName, int choice);
    boolean clearRecipeList(boolean confirm);
}
