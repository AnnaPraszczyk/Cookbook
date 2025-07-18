package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRecipeAdapterTest {
    @Mock
    private SpringDataRecipeRepository jpaRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private UpdateRecipeAdapter adapter;

    private Recipe domain;
    private RecipeEntity entity;
    private RecipeEntity updatedEntity;
    private Recipe mappedBack;

    @BeforeEach
    void setup() {
        UUID recipeId = UUID.randomUUID();
        domain = Recipe.newRecipe(recipeId, "Dumplings", Category.MAIN_COURSE, List.of(), "Bake", 3, List.of("traditional"));
        entity = RecipeEntity.newRecipeEntity(recipeId, "Dumplings", Category.MAIN_COURSE, List.of(), "Bake", 3, List.of("traditional"));
        updatedEntity = RecipeEntity.newRecipeEntity(recipeId, "Dumplings", Category.MAIN_COURSE, List.of(), "Bake", 3, List.of("traditional"));
        mappedBack = Recipe.newRecipe(recipeId, "Dumplings", Category.MAIN_COURSE, List.of(), "Bake", 3, List.of("traditional"));
    }

    @Test
    void updateRecipeAndReturnMappedVersion() {
        when(recipeMapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(updatedEntity);
        when(recipeMapper.toDomain(updatedEntity)).thenReturn(mappedBack);
        Recipe result = adapter.updateRecipe(domain);

        assertNotNull(result);
        assertEquals("Dumplings", result.getRecipeName());
        assertEquals(Category.MAIN_COURSE, result.getCategory());
        assertEquals(3, result.getNumberOfServings());
        assertEquals("Bake", result.getInstructions());
        assertEquals(List.of("traditional"), result.getTags());
        verify(recipeMapper).toEntity(domain);
        verify(jpaRepository).save(entity);
        verify(recipeMapper).toDomain(updatedEntity);
    }

    @Test
    void shouldThrowExceptionIfMapperFails() {
        Recipe broken = mock(Recipe.class);
        when(recipeMapper.toEntity(broken))
                .thenThrow(new RecipeValidationException("Recipe data is invalid"));

        assertThrows(RecipeValidationException.class, () -> adapter.updateRecipe(broken));
    }
}