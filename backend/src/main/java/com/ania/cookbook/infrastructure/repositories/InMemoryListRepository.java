package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.domain.repositories.list.DeleteList;
import com.ania.cookbook.domain.repositories.list.ReadList;
import com.ania.cookbook.domain.repositories.list.SaveList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Profile("test")
public class InMemoryListRepository implements SaveList, ReadList, DeleteList {
    private final Map<String, SavedList> storage = new HashMap<>();

    @Override
    public SavedList save(SavedList list) {
        storage.put(list.getListName().name(), list);
        return list;
    }

    @Override
    public Optional<SavedList> findByName(ListName listName) {
        return Optional.ofNullable(storage.get(listName.name()));
    }

    @Override
    public boolean existsByName(ListName listName) {
        return storage.containsKey(listName.name());
    }

    @Override
    public List<ListName> getAllLists() {
        return storage.values().stream()
                .map(SavedList::getListName)
                .toList();
    }

    @Override
    public void delete(SavedList list) {
        storage.remove(list.getListName().name());
    }
}
