package com.ania.cookbook.domain.repositories.list;

import com.ania.cookbook.domain.model.ListEntry;

import java.util.UUID;

public interface UpdateEntry {
    void updateEntry(ListEntry entry);
    ListEntry updateRecipeEntry(UUID entryId, Integer portions);

}
