package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.model.RecipeListEntryDomain;
import com.ania.cookbook.domain.model.SavedRecipeListDomain;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class SavedRecipeListMapper {

    public SavedRecipeListDomain toDomain(SavedRecipeList entity, Function<RecipeListEntry, RecipeListEntryDomain> entryMapper) {
        Objects.requireNonNull(entity, "SavedRecipeList cannot be null");
        List<RecipeListEntryDomain> mappedEntries = Optional.ofNullable(entity.getEntries())
                .orElse(Collections.emptyList())
                .stream()
                .map(entryMapper)
                .toList();

        return SavedRecipeListDomain.builder()
                .listName(new ListName(entity.getListName()))
                .createdAt(entity.getCreatedAt())
                .listDescription(entity.getListDescription())
                .expectedPortions(entity.getExpectedPortions())
                .entries(mappedEntries)
                .build();
    }


    public SavedRecipeList toEntity(SavedRecipeListDomain domain, List<RecipeListEntry> mappedEntries) {
        Objects.requireNonNull(domain, "SavedRecipeListDomain cannot be null");
        List<RecipeListEntry> safeEntries = Optional.ofNullable(mappedEntries)
                .orElse(Collections.emptyList());
        return SavedRecipeList.builder()
                .listName(domain.getListName().name())
                .createdAt(domain.getCreatedAt())
                .listDescription(domain.getListDescription())
                .expectedPortions(domain.getExpectedPortions())
                .entries(safeEntries)
                .build();
    }
}
