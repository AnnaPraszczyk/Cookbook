package com.ania.cookbook.web.controllers.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.application.services.implementations.list.ListService;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.mapper.ListEntryResponseMapper;
import com.ania.cookbook.infrastructure.mapper.ListResponseMapper;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.list.ListRequest;
import com.ania.cookbook.web.list.ListResponse;
import jakarta.validation.Valid;
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
    private final ListService listService;
    private final ListEntryResponseMapper entryResponseMapper;
    private final ListResponseMapper listResponseMapper;

    @PostMapping
    public ResponseEntity<Void> createRecipeList(@Valid @RequestBody ListRequest request) {
        ListName listName = new ListName(request.getListName());
        String description = request.getListDescription();
        Integer expectedPortions = request.getPortions();
        listService.createRecipeList(listName, description, expectedPortions);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{listName}/recipes")
    public ResponseEntity<ListEntryResponse> addRecipeToList(@PathVariable String listName, @Valid @RequestBody ListRequest request) {
        ListEntry entry = listService.addRecipeToList(request.getRecipeId(), new ListName(listName), request.getPortions());
        ListEntryResponse response = entryResponseMapper.toResponse(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getAllLists() {
        List<String> names = listService.getAllLists().stream()
                .map(ListName::name)
                .toList();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/{listName}")
    public ResponseEntity<ListResponse> getRecipesList(@PathVariable String listName) {
        SavedList savedList = listService.getRecipesList(new ListName(listName));
        ListResponse response = listResponseMapper.from(savedList);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{listName}/entries/{entryId}")
    public ResponseEntity<Void> removeRecipeFromList(@PathVariable String listName, @PathVariable UUID entryId) {
        listService.removeRecipeFromList(entryId, new ListName(listName));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{listName}/clear")
    public ResponseEntity<Boolean> clearRecipeList(@PathVariable String listName, @RequestParam boolean confirm) {
        boolean cleared = listService.clearRecipeList(new ListName(listName), confirm);
        return ResponseEntity.ok(cleared);
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> deleteRecipeList(@PathVariable String listName) {
        listService.deleteRecipeList(new ListName(listName));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{listName}/shopping")
    public ResponseEntity<Map<String, Float>> generateShoppingList(@PathVariable String listName) {
        Map<String, Float> shoppingList = listService.generateShoppingList(new ListName(listName));
        return ResponseEntity.ok(shoppingList);
    }
}
