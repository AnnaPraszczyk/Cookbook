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
class SaveRecipeAdapterTest {
    @Mock
    private SpringDataRecipeRepository jpaRepo;

    @Mock
    private RecipeMapper mapper;

    @InjectMocks
    private SaveRecipeAdapter adapter;

    private UUID recipeId;
    private Recipe domain;
    private RecipeEntity entity;
    private RecipeEntity savedEntity;
    private Recipe mappedBack;

    @BeforeEach
    void setup() {
        recipeId = UUID.randomUUID();
        domain = Recipe.newRecipe(recipeId, "Pancakes", Category.DESSERT, List.of(), "Fry", 2, List.of("easy", "fast"));
        entity = RecipeEntity.newRecipeEntity(recipeId, "Pancakes", Category.DESSERT, List.of(), "Fry", 2, List.of("easy", "fast"));
        savedEntity = RecipeEntity.newRecipeEntity(recipeId, "Pancakes", Category.DESSERT, List.of(), "Fry", 2, List.of("easy", "fast"));
        mappedBack = Recipe.newRecipe(recipeId, "Pancakes", Category.DESSERT, List.of(), "Fry", 2, List.of("easy", "fast"));
    }

    @Test
    void saveRecipeAndReturnMappedResult() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepo.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(mappedBack);
        Recipe result = adapter.saveRecipe(domain);

        assertEquals("Pancakes", result.getRecipeName());
        assertEquals(Category.DESSERT, result.getCategory());
        assertEquals(2, result.getNumberOfServings());
        assertEquals(List.of("easy", "fast"), result.getTags());
        assertEquals(recipeId, result.getRecipeId());
        verify(mapper).toEntity(domain);
        verify(jpaRepo).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void throwExceptionIfMapperFails() {
        Recipe broken = mock(Recipe.class);
        when(mapper.toEntity(broken))
                .thenThrow(new RecipeValidationException("Recipe name cannot be null or empty."));

        assertThrows(RecipeValidationException.class, () -> adapter.saveRecipe(broken));
    }


}