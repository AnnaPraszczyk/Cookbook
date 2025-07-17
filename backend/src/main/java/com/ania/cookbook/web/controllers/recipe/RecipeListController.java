package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.ania.cookbook.web.recipe.RecipeListRequest;
import com.ania.cookbook.web.recipe.RecipeListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class RecipeListController {
    private final RecipeManagementService recipeManagementService;

    @PostMapping
    public ResponseEntity<Void> createRecipeList(@RequestBody RecipeListRequest request) {
            recipeManagementService.createRecipeList(new ListName(request.getListName()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{listName}/recipes")
    public ResponseEntity<Void> addRecipeToList(@PathVariable String listName, @RequestBody RecipeListRequest request) {
        recipeManagementService.addRecipeToList(request.getRecipeId(), new ListName(listName));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getAllLists() {
        List<String> names = recipeManagementService.getAllLists().stream()
                .map(ListName::name)
                .toList();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/{listName}")
    public ResponseEntity<RecipeListResponse> getRecipesList(@PathVariable String listName) {
        List<Recipe> recipes = recipeManagementService.getRecipesList(new ListName(listName));
        List<ReadRecipeResponse> recipeResponses = recipes.stream()
                .map(ReadRecipeResponse::from)
                .toList();
        RecipeListResponse response = RecipeListResponse.builder()
                .listName(new ListName(listName))
                .recipes(recipeResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{listName}/recipes/{recipeId}")
    public ResponseEntity<Void> removeRecipeFromList(@PathVariable String listName, @PathVariable UUID recipeId) {
        recipeManagementService.removeRecipeFromList(recipeId, new ListName(listName));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{listName}/clear")
    public ResponseEntity<Boolean> clearRecipeList(@PathVariable String listName, @RequestParam boolean confirm) {
        boolean cleared = recipeManagementService.clearRecipeList(new ListName(listName), confirm);
        return ResponseEntity.ok(cleared);
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> deleteRecipeList(@PathVariable String listName) {
        recipeManagementService.deleteRecipeList(new ListName(listName));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listName}/shopping")
    public ResponseEntity<Map<String, Float>> generateShoppingList(@PathVariable String listName) {
        Map<String, Float> shoppingList = recipeManagementService.generateShoppingList(new ListName(listName));
        return ResponseEntity.ok(shoppingList);
    }
}
