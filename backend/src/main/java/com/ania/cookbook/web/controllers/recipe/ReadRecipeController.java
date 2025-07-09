package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class ReadRecipeController {
    private final FindRecipeUseCase finder;


    @GetMapping("/latest")
    public ResponseEntity<List<ReadRecipeResponse>> getLatestRecipes() {
        List<Recipe> latest = finder.getLatestRecipes(12);
        return ResponseEntity.ok(latest.stream().map(ReadRecipeResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadRecipeResponse> getRecipeById(@PathVariable UUID id) {
        Recipe recipe = finder.findRecipeById(id).orElseThrow(()
                -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        return ResponseEntity.ok(ReadRecipeResponse.from(recipe));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsRecipeById(@PathVariable UUID id) {
        boolean exists = finder.existsRecipeById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByName(@RequestParam("name") String recipeName) {
        List<Recipe> recipes = finder.findRecipeByName(recipeName);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byName/exists")
    public ResponseEntity<Boolean> existsRecipeByName(@RequestParam("name") String recipeName) {
        boolean exists = finder.existsRecipeByName(recipeName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/byCategory")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByCategory(@RequestParam String category) {
        Category cat = resolveCategory(category);
        List<Recipe> recipes = finder.findRecipeByCategory(cat);
        return ResponseEntity.ok(recipes.stream().map(ReadRecipeResponse::from).toList());
    }


    @GetMapping("/search")
    public Page<ReadRecipeResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @PageableDefault(sort = "recipeName") Pageable pageable) {
        try {
            if (name != null && !name.isBlank()) {
                return finder.findRecipeByName(name, pageable)
                        .map(ReadRecipeResponse::from);
            }
            if (category != null && !category.isBlank()) {
                Category cat = resolveCategory(category);
                return finder.findRecipeByCategory(cat, pageable)
                        .map(ReadRecipeResponse::from);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either name or category");
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid category value: {}", category);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category: " + category, ex);
        } catch (Exception e) {
            log.error("❌ Internal error during recipe search", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }

    private Category resolveCategory(String categoryValue) {
        return Arrays.stream(Category.values())
                .filter(cat -> cat.name().equalsIgnoreCase(categoryValue.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryValue));
    }

}