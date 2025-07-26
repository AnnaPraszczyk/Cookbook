package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.infrastructure.mapper.RecipeListEntryMapper;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.web.recipe.RecipeListEntryResponse;
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
    private final RecipeMapper recipeMapper;
    private final RecipeListEntryMapper entryMapper;

    @PostMapping
    public ResponseEntity<Void> createRecipeList(@RequestBody RecipeListRequest request) {
        ListName listName = new ListName(request.getListName());
        String description = request.getListDescription();
        Integer expectedPortions = request.getPortions();
        recipeManagementService.createRecipeList(listName, description, expectedPortions);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{listName}/recipes")
    public ResponseEntity<RecipeListEntryResponse> addRecipeToList(@PathVariable String listName, @RequestBody RecipeListRequest request) {
        RecipeListEntry entry = recipeManagementService.addRecipeToList(request.getRecipeId(), new ListName(listName), request.getPortions());
        RecipeListEntryResponse response = entryMapper.toResponse(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
        RecipeListResponse response = recipeManagementService.getRecipeListResponse(new ListName(listName));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{listName}/entries/{entryId}")
    public ResponseEntity<Void> removeRecipeFromList(@PathVariable String listName, @PathVariable UUID entryId) {
        recipeManagementService.removeRecipeFromList(entryId, new ListName(listName));
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
