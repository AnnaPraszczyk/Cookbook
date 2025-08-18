package com.ania.cookbook.infrastructure.persistence.list;

import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedListRepository extends JpaRepository<SavedListEntity, String> {
    Optional<SavedListEntity> findByListName(String listName);
    boolean existsByListName(String listName);
}
