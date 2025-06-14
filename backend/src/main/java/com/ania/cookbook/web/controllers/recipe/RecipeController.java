package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase.CreateRecipe;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody RecipeRequest request) {
        Recipe recipe = recipeService.createRecipe(
                new CreateRecipe(
                        request.recipeName(),
                        request.category(),
                        request.ingredients(),
                        request.instructions(),
                        request.numberOfServings(),
                        request.tags()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(recipe));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable UUID recipeId,
            @RequestBody UpdateRecipeCase request) {

        Recipe updatedRecipe = recipeService.updateRecipe(recipeId, request);
        return ResponseEntity.ok(mapToResponse(updatedRecipe));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@RequestBody @Valid DeleteRecipeCase request) {
        recipeService.deleteRecipe(request);
        return ResponseEntity.noContent().build();
    }

    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getNumberOfServings(),
                recipe.getTags()
        );
    }
}
