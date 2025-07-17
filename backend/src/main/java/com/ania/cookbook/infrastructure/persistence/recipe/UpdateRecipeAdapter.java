package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UpdateRecipeAdapter implements UpdateRecipe {
    private final SpringDataRecipeRepository jpaRepository;
    private final RecipeMapper recipeMapper;

    @Override
    @Transactional
    public Recipe updateRecipe(Recipe recipe) {
        RecipeEntity entity = recipeMapper.toEntity(recipe);
        RecipeEntity updated = jpaRepository.save(entity);
        return recipeMapper.toDomain(updated);
    }
}
