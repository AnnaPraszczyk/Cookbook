package com.ania.cookbook.domain.repositories.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import java.util.UUID;

public interface DeleteEntry {
    void deleteById(UUID entryId);
    void deleteAllByListName(ListName listName);
}
