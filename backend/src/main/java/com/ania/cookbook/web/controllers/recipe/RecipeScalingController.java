package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.recipe.RecipeScalingService;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.ania.cookbook.web.recipe.RecipeScalingRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/scaling")
@RequiredArgsConstructor
@Validated
public class RecipeScalingController {
    private final RecipeScalingService recipeScalingService;
    private final RecipeResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<RecipeResponse> scaleRecipe(@RequestBody @Valid RecipeScalingRequest request) {
        AdjustRecipe adjustRecipe = new AdjustRecipe(request.getRecipeId(), request.getServings());
        Recipe scaledRecipe = recipeScalingService.adjustRecipeByServings(adjustRecipe);
        RecipeResponse response = responseMapper.toResponse(scaledRecipe);
        return ResponseEntity.ok(response);
    }
}
