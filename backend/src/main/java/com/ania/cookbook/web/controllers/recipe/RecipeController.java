package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.recipe.CreateRecipe;
import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.mappers.RecipeRequestMapper;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Validated
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeRequestMapper recipeRequestMapper;
    private final RecipeResponseMapper recipeResponseMapper;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest request) {
        CreateRecipe createRecipe = recipeRequestMapper.toCreateRecipe(request);
        Recipe recipe = recipeService.createRecipe(createRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeResponseMapper.toResponse(recipe));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable @NotNull UUID recipeId, @Valid @RequestBody UpdateRecipeCase request) {
        Recipe updatedRecipe = recipeService.updateRecipe(recipeId, request);
        return ResponseEntity.ok(recipeResponseMapper.toResponse(updatedRecipe));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable @NotNull UUID recipeId, @Valid @RequestBody DeleteRecipeCase request) {
        if (!recipeId.equals(request.recipeId())) {
            throw new RecipeValidationException("Recipe ID in path and body do not match.");
        }
        recipeService.deleteRecipe(request);
        return ResponseEntity.noContent().build();
    }
}
