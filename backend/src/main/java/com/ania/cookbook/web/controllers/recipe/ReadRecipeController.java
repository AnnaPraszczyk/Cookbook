package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.ReadRecipeService;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.recipe.ReadRecipeRequest;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class ReadRecipeController {
    private final ReadRecipeService readRecipeService;

    @GetMapping("/{id}")
    public ResponseEntity<ReadRecipeResponse> getRecipeById(@PathVariable UUID id) {
        Recipe recipe = readRecipeService.findRecipeById(id).orElseThrow(()
                -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        return ResponseEntity.ok(ReadRecipeResponse.from(recipe));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsRecipeById(@PathVariable UUID id) {
        boolean exists = readRecipeService.existsRecipeById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByName(@RequestParam("name") String recipeName) {
        List<Recipe> recipes = readRecipeService.findRecipeByName(recipeName);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byName/exists")
    public ResponseEntity<Boolean> existsRecipeByName(@RequestParam("name") String recipeName) {
        boolean exists = readRecipeService.existsRecipeByName(recipeName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/byCategory")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByCategory(@RequestParam("category") String categoryStr) {
        Category category = Category.valueOf(categoryStr.toUpperCase());
        List<Recipe> recipes = readRecipeService.findRecipeByCategory(category);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byTag")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByTag(@RequestParam("tag") String tag) {
        List<Recipe> recipes = readRecipeService.findRecipeByTag(tag);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchRecipe(@RequestBody ReadRecipeRequest request) {
        if (request.getRecipeId() != null) {
            Recipe recipe = readRecipeService.findRecipeById(request.getRecipeId()).orElseThrow(()
                    -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
            return ResponseEntity.ok(ReadRecipeResponse.from(recipe));
        } else if (request.getRecipeName() != null && !request.getRecipeName().isBlank()) {
            List<Recipe> recipes = readRecipeService.findRecipeByName(request.getRecipeName());
            List<ReadRecipeResponse> responses = recipes.stream()
                    .map(ReadRecipeResponse::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } else if (request.getCategory() != null && !request.getCategory().isBlank()) {
            Category category = Category.valueOf(request.getCategory().toUpperCase());
            List<Recipe> recipes = readRecipeService.findRecipeByCategory(category);
            List<ReadRecipeResponse> responses = recipes.stream()
                    .map(ReadRecipeResponse::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } else if (request.getTag() != null && !request.getTag().isBlank()) {
            List<Recipe> recipes = readRecipeService.findRecipeByTag(request.getTag());
            List<ReadRecipeResponse> responses = recipes.stream()
                    .map(ReadRecipeResponse::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } else {
            return ResponseEntity.badRequest().body("At least one search parameter must be provided.");
        }
    }
}
