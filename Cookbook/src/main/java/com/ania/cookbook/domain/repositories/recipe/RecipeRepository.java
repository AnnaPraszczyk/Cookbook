package com.ania.cookbook.domain.repositories.recipe;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, UUID> {

    RecipeEntity saveRecipe(RecipeEntity recipe);

    Optional<RecipeEntity> findRecipeById(UUID id);
    Optional<RecipeEntity> findRecipeByName(String name);
    Optional<RecipeEntity> findRecipeByCategoryContains(String category);
    Optional<RecipeEntity> findRecipeByIngredientContains(String ingredient);
    Optional<RecipeEntity> findRecipeByProduct(Product product);
    Optional<RecipeEntity> findRecipeByCreatedAfter(Instant created);

    RecipeEntity updateRecipe(RecipeEntity recipe);

    void deleteRecipeById(UUID id);

    /*@Query("""
            SELECT n FROM Recipe n
            WHERE n.name = :name
            """)
    List<Recipe> findRecipe(@Param("name") String name);

    @Query("""
            SELECT n FROM Recipe n
            WHERE n.category = :category
            """)
    List<Recipe> findRecipe(@Param("category") String category);
    List<Recipe> findRecipe(@Param("name") String name, @Param("category") String category);*/

}
