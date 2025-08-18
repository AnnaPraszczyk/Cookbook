package com.ania.cookbook.domain.repositories.list;
import com.ania.cookbook.domain.model.ListEntry;

public interface SaveEntry {
    ListEntry save(ListEntry entry);
}
