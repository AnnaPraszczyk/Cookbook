package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Primary
public interface RecipeRepository extends JpaRepository<Recipe, UUID>, ReadRecipe {
    Page<Recipe> findByRecipeNameContainingIgnoreCase(String name, Pageable p);
    Page<Recipe> findByCategory(Category category, Pageable p);
    @Query("""
        select r from RecipeEntity r
        join r.tags t
        where lower(t) = lower(:tag)
        """)
    Page<Recipe> findByTag(@Param("tag") String tag, Pageable p);


}
