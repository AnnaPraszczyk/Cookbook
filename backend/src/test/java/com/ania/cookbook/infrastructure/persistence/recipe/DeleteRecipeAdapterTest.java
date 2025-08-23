package com.ania.cookbook.infrastructure.persistence.recipe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeAdapterTest {
    @Mock
    SpringDataRecipeRepository jpaRepository;

    @InjectMocks
    DeleteRecipeAdapter adapter;

    @Test
    void deleteRecipeById() {
        UUID recipeId = UUID.randomUUID();
        adapter.deleteRecipeById(recipeId);

        verify(jpaRepository).deleteById(recipeId);
    }
}