package com.ania.cookbook.infrastructure.persistence.list;

import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedRecipeListRepository extends JpaRepository<SavedRecipeList, String> {
    Optional<SavedRecipeList> findByListName(String listName);
    boolean existsByListName(String listName);

}
