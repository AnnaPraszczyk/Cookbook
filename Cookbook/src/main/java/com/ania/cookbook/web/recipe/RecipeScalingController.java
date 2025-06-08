package com.ania.cookbook.web.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeScalingService;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/scaling")
@RequiredArgsConstructor
public class RecipeScalingController {
    private final RecipeScalingService recipeScalingService;

    @PostMapping
    public ResponseEntity<RecipeScalingResponse> scaleRecipe(@RequestBody RecipeScalingRequest request) {
        AdjustRecipe adjustRecipe = new AdjustRecipe(request.getRecipeId(), request.getServings());

        Recipe scaledRecipe = recipeScalingService.adjustRecipeByServings(adjustRecipe);

        RecipeScalingResponse response = RecipeScalingResponse.from(scaledRecipe);
        return ResponseEntity.ok(response);
    }
}
