package com.ania.cookbook.infrastructure.controllers;

import com.ania.cookbook.application.services.CreateRecipieUseCase;
import com.ania.cookbook.application.services.ProductService;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.dto.CreateRecipeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
class RecipeController {
    private final CreateRecipieUseCase recipeService;
    private final ProductService productService;

    @PostMapping
    ResponseEntity<CreationRecepieResponse> createRecipe(@RequestBody CreateRecipeRequest request) {
        productService.saveProduct(new Product());
        recipeService.createRecipie();
        return modelMapping.map(saveRecipe);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecipeDetails getRecipe(@PathVariable("id") UUID id) throws RecipeException {
        var recipe = recipeService.getRecipe(id);
        return modelMapping.map(recipe);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    Collection<RecipeDetails> getRecipesForName(@RequestParam("name") String name) {
        var matchedRecipes = recipeService.getByRecipeName(name);
        return matchedRecipes.stream()
                .map(modelMapping::map)
                .toList();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecipeDetails updateRecipe(@PathVariable("id") UUID id, @RequestBody String name) throws RecipeException {
        var recipe = recipeService.changeRecipe(id, name);
        return modelMapping.map(recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRecipe(@PathVariable("id") UUID id) throws RecipeException {
        recipeService.cancel(id);
    }

    @ExceptionHandler(RecipeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleRecipeException(RecipeException e) {
        return e.getMessage();
    }*/


