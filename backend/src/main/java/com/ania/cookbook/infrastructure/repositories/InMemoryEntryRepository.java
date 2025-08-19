package com.ania.cookbook.infrastructure.repositories;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.repositories.list.DeleteEntry;
import com.ania.cookbook.domain.repositories.list.ReadEntry;
import com.ania.cookbook.domain.repositories.list.SaveEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.*;

@RequiredArgsConstructor
@Repository
@Profile("test")
public class InMemoryEntryRepository implements SaveEntry, ReadEntry, DeleteEntry {
    private final Map<UUID, ListEntry> entries = new HashMap<>();

    @Override
    public ListEntry save(ListEntry entry) {
        entries.put(entry.getEntryId(), entry);
        return entry;
    }

    @Override
    public Optional<ListEntry> findByRecipeIdAndListName(UUID recipeId, ListName listName) {
        return entries.values().stream()
                .filter(entry ->
                        entry.getRecipe().getRecipeId().equals(recipeId) &&
                                entry.getSavedRecipeList().getListName().equals(listName))
                .findFirst();
    }

    @Override
    public List<ListEntry> findByListName(ListName listName) {
        return entries.values().stream()
                .filter(entry -> entry.getSavedRecipeList().getListName().equals(listName))
                .toList();
    }

    @Override
    public Optional<ListEntry> findById(UUID entryId) {
        return Optional.ofNullable(entries.get(entryId));
    }

    @Override
    public boolean existsByRecipeId(UUID recipeId) {
        return entries.values().stream()
                .anyMatch(entry -> entry.getRecipe().getRecipeId().equals(recipeId));


    }

    @Override
    public void deleteById(UUID entryId) {
        entries.remove(entryId);
    }

    @Override
    public void deleteAllByListName(ListName listName) {
        List<UUID> toRemove = entries.values().stream()
                .filter(entry -> entry.getSavedRecipeList().getListName().equals(listName))
                .map(ListEntry::getEntryId)
                .toList();
        toRemove.forEach(entries::remove);
    }
}
