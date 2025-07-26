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
import com.ania.cookbook.infrastructure.mapper.RecipeListEntryMapper;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import com.ania.cookbook.infrastructure.persistence.list.RecipeListEntryRepository;
import com.ania.cookbook.infrastructure.persistence.list.SavedRecipeListRepository;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.ania.cookbook.web.recipe.RecipeListEntryResponse;
import com.ania.cookbook.web.recipe.RecipeListResponse;
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
    private final RecipeListEntryMapper entryMapper;

    @Override
    public void createRecipeList(ListName list, String description, Integer defaultPortions) {
        if (listRepository.existsByListName(list.name())) {
            throw new ListValidationException("Recipe list already exists.");
        }
        int safePortions = defaultPortions != null ? defaultPortions : 1;
        SavedRecipeList savedList = SavedRecipeList.builder()
                .listName(list.name())
                .createdAt(Instant.now())
                .expectedPortions(safePortions)
                .listDescription(description != null ? description.trim() : "")
                .entries(new ArrayList<>())
                .build();
        listRepository.save(savedList);
    }

    public static RecipeListResponse from(SavedRecipeList list, RecipeMapper recipeMapper) {
        List<RecipeListEntryResponse> entryResponses = list.getEntries().stream()
                .map(entry -> {
                    Recipe domainRecipe = recipeMapper.toDomain(entry.getRecipe());
                    ReadRecipeResponse recipeResponse = ReadRecipeResponse.from(domainRecipe);

                    return RecipeListEntryResponse.builder()
                            .entryId(entry.getEntryId())
                            .portions(entry.getPortions())
                            .recipe(recipeResponse)
                            .build();
                })
                .toList();

        return RecipeListResponse.builder()
                .listName(new ListName(list.getListName()))
                .listDescription(list.getListDescription())
                .expectedPortions(list.getExpectedPortions())
                .recipes(entryResponses)
                .build();
    }

    @Override
    public RecipeListEntry addRecipeToList(UUID recipeId, ListName list, Integer portions) {
        Optional<RecipeListEntry> existingEntry = entryRepository.findByRecipe_RecipeIdAndSavedList_ListName(recipeId, list.name());
        if (existingEntry.isPresent()) {
            return updateRecipeEntry(existingEntry.get(), portions);
        } else {
            return addNewRecipeEntry(recipeId, list, portions);
        }
    }

    @Override
    public RecipeListEntry updateRecipeEntry(RecipeListEntry entry, Integer portions) {
        if (portions != null && entry.getPortions() != portions) {
            entry.setPortions(portions);
            entryRepository.save(entry);
        }
        return entry;
    }

    private RecipeListEntry addNewRecipeEntry(UUID recipeId, ListName list, Integer portions) {
        Recipe recipe = readRecipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found."));
        SavedRecipeList savedList = listRepository.findByListName(list.name())
                .orElseThrow(() -> new ListNotFoundException("List not found."));
        RecipeEntity entity = recipeMapper.toEntity(recipe);
        int finalPortions = portions != null ? portions : recipe.getNumberOfServings();

        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(entity)
                .savedList(savedList)
                .portions(finalPortions)
                .build();

        return entryRepository.save(entry);
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
    public List<RecipeListEntry> getRecipesListEntries(ListName list) {
        boolean exists = listRepository.existsByListName(list.name());
        if (!exists) {
            throw new ListNotFoundException("Recipe list with the given name does not exist.");
        }
        return entryRepository.findBySavedList_ListName(list.name());
    }
    @Override
    public RecipeListResponse getRecipeListResponse(ListName listName) {
        List<RecipeListEntry> entries = getRecipesListEntries(listName);
        List<RecipeListEntryResponse> responses = entries.stream()
                .map(entryMapper::toResponse)
                .toList();
        return RecipeListResponse.builder()
                .listName(listName)
                .recipes(responses)
                .build();
    }

    @Override
    public void removeRecipeFromList(UUID entryId, ListName list) {
        if (entryId == null) {
            throw new RecipeValidationException("Recipe ID cannot be null.");
        }
        SavedRecipeList savedList = listRepository.findByListName(list.name())
                .orElseThrow(() -> new ListNotFoundException("Recipe list with the given name does not exist."));
        RecipeListEntry entryToRemove = entryRepository.findById(entryId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found in list."));
        if (!entryToRemove.getSavedList().equals(savedList)) {
            throw new RecipeValidationException("Recipe entry does not belong to the specified list.");
        }
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
        if (!listRepository.existsByListName(list.name())) {
            throw new ListNotFoundException("List not found.");
        }
        if (entries.isEmpty()) {
            return Map.of();
        }
        Map<String, Float> shoppingList = new HashMap<>();
        for (RecipeListEntry entry : entries) {
            Recipe recipe = recipeMapper.toDomain(entry.getRecipe());
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

    @Override
    public List<ListName> getAllLists() {
        return listRepository.findAll().stream()
                .map(list -> new ListName(list.getListName()))
                .toList();
    }
}

