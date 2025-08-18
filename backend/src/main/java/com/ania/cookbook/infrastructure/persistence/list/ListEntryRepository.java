package com.ania.cookbook.infrastructure.persistence.list;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListEntryRepository extends JpaRepository<ListEntryEntity, UUID> {
    List<ListEntryEntity> findBySavedList_ListName(String listName);
    boolean existsByRecipe_RecipeId(UUID recipeId);
    void deleteBySavedList_ListName(String listName);
    Optional<ListEntryEntity> findByRecipe_RecipeIdAndSavedList_ListName(UUID id, String listName);
}
