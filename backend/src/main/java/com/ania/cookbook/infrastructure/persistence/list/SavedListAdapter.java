package com.ania.cookbook.infrastructure.persistence.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.domain.repositories.list.DeleteList;
import com.ania.cookbook.domain.repositories.list.ReadList;
import com.ania.cookbook.domain.repositories.list.SaveList;
import com.ania.cookbook.infrastructure.mapper.SavedListMapper;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SavedListAdapter implements SaveList, ReadList, DeleteList {
    private final SavedListRepository repository;
    private final SavedListMapper mapper;

    @Override
    public SavedList save(SavedList list) {
        SavedListEntity entity = mapper.toEntity(list);
        SavedListEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<SavedList> findByName(ListName listName) {
        return repository.findByListName(listName.name())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(ListName listName) {
        return repository.existsByListName(listName.name());
    }

    @Override
    public List<ListName> getAllLists() {
        return repository.findAll().stream()
                .map(entity -> new ListName(entity.getListName()))
                .toList();
    }

    @Override
    public void delete(SavedList list) {
        SavedListEntity entity = mapper.toEntity(list);
        repository.delete(entity);
    }

}
