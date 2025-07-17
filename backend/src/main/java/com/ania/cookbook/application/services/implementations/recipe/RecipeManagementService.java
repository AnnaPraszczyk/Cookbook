package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import com.ania.cookbook.infrastructure.persistence.list.RecipeListEntryRepository;
import com.ania.cookbook.infrastructure.persistence.list.SavedRecipeListRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

@Getter
@RequiredArgsConstructor
@Service
public class RecipeManagementService implements ListManagementUseCase {
    private final SaveRecipe saveRecipeRepository;
    private final ReadRecipe readRecipeRepository;
    private final List<Recipe> recipeList = new ArrayList<>();
    private final RecipeListEntryRepository entryRepository;
    private final SavedRecipeListRepository listRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public void createRecipeList(ListName list) {
        if (listRepository.existsByListName(list.name())) {
            throw new ListValidationException("Recipe list already exists.");
        }
        SavedRecipeList savedList = SavedRecipeList.builder()
                .listName(list.name())
                .createdAt(Instant.now())
                .expectedPortions(0)
                .listDescription("")
                .entries(new ArrayList<>())
                .build();
        listRepository.save(savedList);
    }

    @Override
    public void addRecipeToList(UUID recipeId, ListName list) {
        Recipe recipe = readRecipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found."));
        SavedRecipeList savedList = listRepository.findByListName(list.name())
                .orElseThrow(() -> new ListNotFoundException("List not found."));
        boolean alreadyExists = entryRepository.findBySavedList_ListName(list.name()).stream()
                .anyMatch(entry -> entry.getRecipe().getRecipeId().equals(recipeId));
        if (alreadyExists) return;
        RecipeEntity entity = recipeMapper.toEntity(recipe);
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(entity)
                .savedList(savedList)
                .portions(recipe.getNumberOfServings())
                .build();
        entryRepository.save(entry);
    }

    @Override
    public List<Recipe> getRecipesList(ListName list) {
        boolean exists = listRepository.existsById(list.name());
        if (!exists) {
            throw new ListNotFoundException("Recipe list with the given name does not exist.");
        }
        return entryRepository.findBySavedList_ListName(list.name()).stream()
                .map(entry -> recipeMapper.toDomain(entry.getRecipe()))
                .toList();
    }

    @Override
    public void removeRecipeFromList(UUID recipeId, ListName list) {
        if (recipeId == null) {
            throw new RecipeValidationException("Recipe ID cannot be null.");
        }
        listRepository.findByListName(list.name())
                .orElseThrow(() -> new ListNotFoundException("Recipe list with the given name does not exist."));
        List<RecipeListEntry> entries = entryRepository.findBySavedList_ListName(list.name());

        RecipeListEntry entryToRemove = entries.stream()
                .filter(e -> e.getRecipe().getRecipeId().equals(recipeId))
                .findFirst()
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found in list."));

        entryRepository.delete(entryToRemove);
    }

    @Override
    public void deleteRecipeList(ListName list) {
        SavedRecipeList savedList = listRepository.findByListName(list.name())
                .orElseThrow(() -> new ListNotFoundException("List not found."));

        listRepository.delete(savedList);
    }

    @Override
    public boolean clearRecipeList(ListName list, boolean confirm) {
        if (!confirm) return false;

        List<RecipeListEntry> entries = entryRepository.findBySavedList_ListName(list.name());

        if (entries.isEmpty()) {
            throw new ListNotFoundException("List is empty or not found.");
        }

        entryRepository.deleteAll(entries);
        return true;
    }

    @Override
    public Map<String, Float> generateShoppingList(ListName list) {
        List<RecipeListEntry> entries = entryRepository.findBySavedList_ListName(list.name());
        if (entries.isEmpty()) {
            return Map.of();
        }
        Map<String, Float> shoppingList = new HashMap<>();
        for (RecipeListEntry entry : entries) {
            Recipe recipe = recipeMapper.toDomain(entry.getRecipe());
            float scale = entry.getPortions() / (float) recipe.getNumberOfServings();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String name = ingredient.getProduct().getProductName().name();
                float amount = scale * ingredient.getUnit().toGrams(ingredient.getAmount());
                shoppingList.merge(name, amount, Float::sum);
            }
        }
        return shoppingList;
    }

    @Override
    public List<ListName> getAllLists() {
        return listRepository.findAll().stream()
                .map(list -> new ListName(list.getListName()))
                .toList();
    }
}

