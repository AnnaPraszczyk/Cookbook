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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class ReadRecipeController {
    private final FindRecipeUseCase finder;


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
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByCategory(@RequestParam("category") String categoryStr) {
        Category category = Category.valueOf(categoryStr.toUpperCase());
        List<Recipe> recipes = finder.findRecipeByCategory(category);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byTag")
    public ResponseEntity<List<ReadRecipeResponse>> getRecipesByTag(@RequestParam("tag") String tag) {
        List<Recipe> recipes = finder.findRecipeByTag(tag);
        List<ReadRecipeResponse> responses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public Page<ReadRecipeResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @PageableDefault(sort = "recipeName") Pageable pageable
    ) {
        if (name != null) {
            return finder.findRecipeByName(name, pageable)
                    .map(ReadRecipeResponse::from);
        }
        if (category != null) {
            var cat = Category.valueOf(category.toUpperCase());
            return finder.findRecipeByCategory(cat, pageable)
                    .map(ReadRecipeResponse::from);
        }
        if (tag != null) {
            return finder.findRecipeByTag(tag, pageable)
                    .map(ReadRecipeResponse::from);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Provide one of: name, category or tag"
        );
    }
}
