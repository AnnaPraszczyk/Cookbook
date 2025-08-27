package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.mappers.CategoryResolver;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class ReadRecipeController {
    private final FindRecipeUseCase finder;
    private final RecipeResponseMapper responseMapper;
    private final CategoryResolver categoryResolver;

    @GetMapping("/latest")
    public ResponseEntity<List<RecipeResponse>> getLatestRecipes() {
        List<Recipe> latest = finder.getLatestRecipes(12);
        return ResponseEntity.ok(responseMapper.toResponseList(latest));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable @NotNull UUID recipeId) {
        Recipe recipe = finder.findRecipeById(recipeId).orElseThrow(()
                -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        RecipeResponse response = responseMapper.toResponse(recipe);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{recipeId}/exists")
    public ResponseEntity<Boolean> existsRecipeById(@PathVariable @NotNull UUID recipeId) {
        return ResponseEntity.ok(finder.existsRecipeById(recipeId));
    }

    @GetMapping("/byName")
    public ResponseEntity<List<RecipeResponse>> getRecipesByName(@RequestParam("recipeName") @NotBlank String recipeName) {
        List<Recipe> recipes = finder.findRecipeByName(recipeName);
        List<RecipeResponse> responses = responseMapper.toResponseList(recipes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/byName/exists")
    public ResponseEntity<Boolean> existsRecipeByName(@RequestParam("recipeName") @NotBlank String recipeName) {
        return ResponseEntity.ok(finder.existsRecipeByName(recipeName));
    }

    @GetMapping("/byCategory")
    public ResponseEntity<List<RecipeResponse>> getRecipesByCategory(@RequestParam @NotNull String category) {
        Category cat = categoryResolver.resolve(category.trim());
        List<Recipe> recipes = finder.findRecipeByCategory(cat);
        return ResponseEntity.ok(responseMapper.toResponseList(recipes));
    }

//    @GetMapping("/search")
//    public ResponseEntity<Page<RecipeResponse>> search(@RequestParam(required = false) String recipeName,
//                                                       @RequestParam(required = false) String category,
//                                                       @PageableDefault(sort = "recipeName") Pageable pageable) {
//        try {
//            if (!isBlank(recipeName)) {
//                Page<Recipe> page = finder.findRecipeByName(recipeName.trim(), pageable);
//                return ResponseEntity.ok(responseMapper.toResponsePage(page));
//
//            }
//            if (!isBlank(category)) {
//                Category cat = categoryResolver.resolve(category.trim());
//                Page<Recipe> page = finder.findRecipeByCategory(cat, pageable);
//                return ResponseEntity.ok(responseMapper.toResponsePage(page));
//            }
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either name or category");
//        } catch (RecipeValidationException ex) {
//            log.warn("Invalid category value: {}", category);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category: " + category, ex);
//        } catch (ResponseStatusException ex) {
//            throw ex;
//        } catch (Exception e) {
//            log.error("Internal error during recipe search", e);
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
//        }
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<RecipeResponse>> search(
            @RequestParam(required = false) String recipeName,
            @RequestParam(required = false) String category,
            @PageableDefault(sort = "recipeName") Pageable pageable
    ) {
        boolean hasName = !isBlank(recipeName);
        boolean hasCategory = !isBlank(category);

        if (hasName && hasCategory) {
            Category cat = categoryResolver.resolve(category.trim());
            return ResponseEntity.ok(responseMapper.toResponsePage(
                            finder.findRecipeByNameAndCategory(recipeName.trim(), cat, pageable)));}
        if (hasName) {
            return ResponseEntity.ok(responseMapper.toResponsePage(
                            finder.findRecipeByName(recipeName.trim(), pageable)));}
        if (hasCategory) {
            Category cat = categoryResolver.resolve(category.trim());
            return ResponseEntity.ok(
                    responseMapper.toResponsePage(
                            finder.findRecipeByCategory(cat, pageable)));}
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide at least name or category");
    }
}