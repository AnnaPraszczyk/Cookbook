package com.ania.cookbook.domain.repositories.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.ListEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadEntry {
    Optional<ListEntry> findByRecipeIdAndListName(UUID recipeId, ListName listName);
    List<ListEntry> findByListName(ListName listName);
    Optional<ListEntry> findById(UUID entryId);
}
