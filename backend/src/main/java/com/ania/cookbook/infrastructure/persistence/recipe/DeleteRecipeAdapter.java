package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeleteRecipeAdapter implements DeleteRecipe {
    private final SpringDataRecipeRepository jpaRepository;

    @Override
    @Transactional
    public void deleteRecipeById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
