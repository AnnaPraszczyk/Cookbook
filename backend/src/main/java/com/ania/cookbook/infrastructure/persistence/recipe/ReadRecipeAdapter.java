package com.ania.cookbook.infrastructure.persistence.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReadRecipeAdapter implements ReadRecipe {
    private final SpringDataRecipeRepository jpaRepository;
    private final RecipeMapper recipeMapper;


    @Override
    public Optional<Recipe> findRecipeById(UUID id) {
        return jpaRepository.findById(id)
                .map(recipeMapper::toDomain);
    }

    @Override
    public boolean existsRecipeById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Recipe> findRecipeByName(String name) {
        return jpaRepository.findByRecipeNameContainingIgnoreCase(name)
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Recipe> findRecipeByName(String name, Pageable pageable) {
        return jpaRepository.findByRecipeNameContainingIgnoreCase(name, pageable)
                .map(recipeMapper::toDomain);
    }

    @Override
    public boolean existsRecipeByName(String name) {
        return jpaRepository.existsByRecipeNameIgnoreCase(name);
    }

    @Override
    public List<Recipe> findRecipeByCategory(Category category) {
        return jpaRepository.findByCategory(category)
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Recipe> findRecipeByCategory(Category category, Pageable pageable) {
        return jpaRepository.findByCategory(category, pageable)
                .map(recipeMapper::toDomain);
    }

    @Override
    public List<Recipe> findTopNByOrderByCreatedDesc(int limit) {
        return jpaRepository
                .findAllByOrderByCreatedDesc(PageRequest.of(0, limit))
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }
}
