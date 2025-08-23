package com.ania.cookbook.application.services.implementations.list;
import com.ania.cookbook.application.services.interfaces.list.ListUseCase;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.domain.repositories.list.*;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;

@Getter
@RequiredArgsConstructor
@Service
@Transactional
public class ListService implements ListUseCase {
    private final SaveList saveList;
    private final ReadList readList;
    private final DeleteList deleteList;
    private final SaveEntry saveEntry;
    private final ReadEntry readEntry;
    private final DeleteEntry deleteEntry;
    private final ReadRecipe readRecipe;

    @Override
    public void createRecipeList(ListName listName, String description, Integer defaultPortions) {
        if (readList.existsByName(listName)) {
            throw new ListValidationException("Recipe list already exists.");
        }
        int safePortions = defaultPortions != null ? defaultPortions : 1;
        SavedList list = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .expectedPortions(safePortions)
                .listDescription(description != null ? description.trim() : "")
                .build();
        saveList.save(list);
    }

    @Override
    public ListEntry addRecipeToList(UUID recipeId, ListName listName, Integer portions) {
        Optional<ListEntry> existingEntry = readEntry.findByRecipeIdAndListName(recipeId, listName);
        if (existingEntry.isPresent()) {
            return updateRecipeEntry(existingEntry.get().getEntryId(), portions);
        }
        Recipe recipe = readRecipe.findRecipeById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        SavedList savedList = readList.findByName(listName)
                .orElseThrow(() -> new ListNotFoundException("List not found"));
        ListEntry entry = ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(portions != null ? portions : 1)
                .build();
        return saveEntry.save(entry);
    }

    @Override
    public ListEntry updateRecipeEntry(UUID entryId, Integer portions) {
        ListEntry entry = readEntry.findById(entryId)
                .orElseThrow(() -> new ListValidationException("Entry not found"));
        int newPortions = portions != null ? portions : entry.getPortions();
        ListEntry updated = ListEntry.builder()
                .entryId(entry.getEntryId())
                .recipe(entry.getRecipe())
                .savedRecipeList(entry.getSavedRecipeList())
                .portions(newPortions)
                .build();
        return saveEntry.save(updated);
    }

    @Override
    public SavedList getRecipesList(ListName listName) {
        SavedList savedList = readList.findByName(listName)
                .orElseThrow(() -> new ListNotFoundException("Recipe list with the given name does not exist."));
        List<ListEntry> entries = readEntry.findByListName(listName);

        return SavedList.builder()
                .listName(savedList.getListName())
                .createdAt(savedList.getCreatedAt())
                .expectedPortions(savedList.getExpectedPortions())
                .listDescription(savedList.getListDescription())
                .entries(entries)
                .build();
    }

    @Override
    public List<ListEntry> getRecipesListEntries(ListName listName) {
        if (!readList.existsByName(listName)) {
            throw new ListNotFoundException("Recipe list with the given name does not exist.");
        }
        return readEntry.findByListName(listName);
    }

    @Override
    public List<ListName> getAllLists() {
        return readList.getAllLists();
    }

    @Override
    public boolean existsRecipeOnListByRecipeId(UUID recipeId){
        return readEntry.existsByRecipeId(recipeId);
    }

    @Override
    public void removeRecipeFromList(UUID entryId, ListName listName) {
        ListEntry entry = readEntry.findById(entryId)
                .orElseThrow(() -> new ListValidationException("Entry not found"));
        if (!entry.getSavedRecipeList().getListName().equals(listName)) {
            throw new ListValidationException("Entry does not belong to the specified list.");
        }
        deleteEntry.deleteById(entryId);
    }


    @Override
    public boolean clearRecipeList(ListName listName, boolean confirm) {
        if (!confirm) return false;
        List<ListEntry> entries = readEntry.findByListName(listName);
        if (entries.isEmpty()) {
            throw new ListNotFoundException("List is empty or not found.");
        }
        deleteEntry.deleteAllByListName(listName);
        return true;
    }

    @Override
    public void deleteRecipeList(ListName listName) {
        SavedList savedList = readList.findByName(listName)
                .orElseThrow(() -> new ListNotFoundException("List not found."));
        deleteList.delete(savedList);
    }

    @Override
    public Map<String, Float> generateShoppingList(ListName listName) {
        if (!readList.existsByName(listName)) {
            throw new ListNotFoundException("List not found.");
        }
        List<ListEntry> entries = readEntry.findByListName(listName);
        if (entries.isEmpty()) {
            return Map.of();
        }
        Map<String, Float> shoppingList = new HashMap<>();
        for (ListEntry entry : entries) {
            Recipe recipe = entry.getRecipe();
            float scale = entry.getPortions() / (float) recipe.getNumberOfServings();
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient == null || ingredient.getProduct() == null || ingredient.getUnit() == null) continue;
                String name = ingredient.getProduct().getProductName().name();
                float amount = scale * ingredient.getUnit().toGrams(ingredient.getAmount());
                shoppingList.merge(name, amount, Float::sum);
            }
        }
        return shoppingList;
    }
}


