package com.ania.cookbook.infrastructure.persistence.list;

import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.repositories.list.DeleteEntry;
import com.ania.cookbook.domain.repositories.list.ReadEntry;
import com.ania.cookbook.domain.repositories.list.SaveEntry;
import com.ania.cookbook.infrastructure.mapper.ListEntryMapper;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ListEntryAdapter implements SaveEntry, ReadEntry, DeleteEntry {
    private final ListEntryRepository repository;
    private final ListEntryMapper mapper;

    @Override
    public ListEntry save(ListEntry entry) {
        ListEntryEntity entity = mapper.toEntity(entry);
        ListEntryEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ListEntry> findByRecipeIdAndListName(UUID recipeId, ListName listName) {
        return repository.findByRecipe_RecipeIdAndSavedList_ListName(recipeId, listName.name())
                .map(mapper::toDomain);
    }

    @Override
    public List<ListEntry> findByListName(ListName listName) {
        return repository.findBySavedList_ListName(listName.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ListEntry> findById(UUID entryId) {
        return repository.findById(entryId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByRecipeId(UUID recipeId) {
        return repository.existsByRecipe_RecipeId(recipeId);
    }


    @Override
    public void deleteById(UUID entryId) {
        repository.deleteById(entryId);
    }

    @Override
    public void deleteAllByListName(ListName listName) {
        repository.deleteBySavedList_ListName(listName.name());
    }
}
