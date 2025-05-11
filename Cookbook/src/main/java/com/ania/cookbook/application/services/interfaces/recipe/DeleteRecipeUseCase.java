package com.ania.cookbook.application.services.interfaces.recipe;

public interface DeleteRecipeUseCase {

    void deleteRecipe(DeleteRecipeRequest request);
    record DeleteRecipeRequest(String recipeName){}
}
