package com.ania.cookbook.application.services.interfaces.recipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface DeleteRecipeUseCase {
    void deleteRecipe(DeleteRecipeCase request);
    record DeleteRecipeCase(
            @NotNull UUID recipeId,
            @NotBlank String recipeName){
    }
}
