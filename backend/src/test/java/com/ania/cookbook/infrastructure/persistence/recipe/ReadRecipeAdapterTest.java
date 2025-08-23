package com.ania.cookbook.infrastructure.persistence.recipe;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadRecipeAdapterTest {
    @Mock
    private SpringDataRecipeRepository jpaRepository;
    @Mock
    private RecipeMapper recipeMapper;
    @InjectMocks
    private ReadRecipeAdapter adapter;

    private RecipeEntity entity;
    private Recipe domain;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = RecipeEntity.newRecipeEntity(id, "Cake", Category.DESSERT, List.of(), "Bake a cake.", 4, List.of("sweet"));
        domain = Recipe.newRecipe(id, "Cake", Category.DESSERT, List.of(), "Bake a cake.", 4, List.of("sweet"));
    }

    @Test
    void findRecipeById() {
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        Optional<Recipe> result = adapter.findRecipeById(id);

        assertTrue(result.isPresent());
        assertEquals("Cake", result.get().getRecipeName());
    }

    @Test
    void returnFalseIfRecipeDoesNotExistById() {
        when(jpaRepository.existsById(id)).thenReturn(false);
        assertFalse(adapter.existsRecipeById(id));
    }

    @Test
    void findRecipesByName() {
        when(jpaRepository.findByRecipeNameContainingIgnoreCase("Cake"))
                .thenReturn(List.of(entity));
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        List<Recipe> result = adapter.findRecipeByName("Cake");

        assertEquals(1, result.size());
        assertEquals("Cake", result.getFirst().getRecipeName());
    }

    @Test
    void findRecipesByNameWithPaging() {
        Page<RecipeEntity> entityPage = new PageImpl<>(List.of(entity));
        when(jpaRepository.findByRecipeNameContainingIgnoreCase(eq("Cake"), any(Pageable.class)))
                .thenReturn(entityPage);
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        Page<Recipe> result = adapter.findRecipeByName("Cake", PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Cake", result.getContent().getFirst().getRecipeName());
    }

    @Test
    void findRecipesByCategory() {
        when(jpaRepository.findByCategory(Category.DESSERT)).thenReturn(List.of(entity));
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        List<Recipe> result = adapter.findRecipeByCategory(Category.DESSERT);

        assertEquals(1, result.size());
        assertEquals(Category.DESSERT, result.getFirst().getCategory());
    }

    @Test
    void findRecipesByCategoryWithPaging() {
        Page<RecipeEntity> entityPage = new PageImpl<>(List.of(entity));
        when(jpaRepository.findByCategory(eq(Category.DESSERT), any(Pageable.class)))
                .thenReturn(entityPage);
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        Page<Recipe> result = adapter.findRecipeByCategory(Category.DESSERT, PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals(Category.DESSERT, result.getContent().getFirst().getCategory());
    }

    @Test
    void findTopRecipesByCreatedDate() {
        when(jpaRepository.findAllByOrderByCreatedDesc(any(Pageable.class)))
                .thenReturn(List.of(entity));
        when(recipeMapper.toDomain(entity)).thenReturn(domain);
        List<Recipe> result = adapter.findTopNByOrderByCreatedDesc(1);

        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getRecipeId());
    }

    @Test
    void checkExistenceByName() {
        when(jpaRepository.existsByRecipeNameIgnoreCase("Cake")).thenReturn(true);
        assertTrue(adapter.existsRecipeByName("Cake"));
    }
}