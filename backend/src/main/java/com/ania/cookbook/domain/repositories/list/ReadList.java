package com.ania.cookbook.domain.repositories.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import java.util.List;
import java.util.Optional;

public interface ReadList {
    Optional<SavedList> findByName(ListName name);
    boolean existsByName(ListName name);
    List<ListName> getAllLists();
}
