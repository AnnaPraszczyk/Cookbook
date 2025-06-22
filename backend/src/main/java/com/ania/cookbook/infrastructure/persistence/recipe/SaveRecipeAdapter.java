package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SaveRecipeAdapter implements SaveRecipe {
    private final SpringDataRecipeRepository jpaRepo;
    private final RecipeMapper mapper;

    @Override
    @Transactional
    public Recipe saveRecipe(Recipe domainRecipe) {
        RecipeEntity entity = mapper.toEntity(domainRecipe);
        RecipeEntity saved =  jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }
}
