package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataRecipeRepository extends JpaRepository<RecipeEntity, UUID> {
    List<RecipeEntity> findByRecipeNameContainingIgnoreCase(String name);
    Page<RecipeEntity> findByRecipeNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByRecipeNameIgnoreCase(String name);

    List<RecipeEntity> findByCategory(Category category);
    Page<RecipeEntity> findByCategory(Category category, Pageable pageable);

}
