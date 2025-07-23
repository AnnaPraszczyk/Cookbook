package com.ania.cookbook.infrastructure.persistence.list;

import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipeListEntryRepository extends JpaRepository<RecipeListEntry, UUID> {
    List<RecipeListEntry> findBySavedList_ListName(String listName);
    void deleteBySavedList_ListName(String listName);
}
