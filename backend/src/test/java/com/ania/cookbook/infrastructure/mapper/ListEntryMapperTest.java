package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListEntryMapperTest {
    @Mock
    private RecipeMapper recipeMapper;
    @Mock
    private SavedListMapperWithoutEntries savedListMapper;

    private ListEntryMapper listEntryMapper;

    @BeforeEach
    void setUp() {
        listEntryMapper = new ListEntryMapper(recipeMapper, savedListMapper);}

    @Test
    void mapEntityToDomainCorrectly() {
        UUID entryId = UUID.randomUUID();
        RecipeEntity recipeEntity = new RecipeEntity();
        SavedListEntity savedListEntity = new SavedListEntity();
        ListEntryEntity entity = ListEntryEntity.builder()
                .entryId(entryId)
                .recipe(recipeEntity)
                .savedList(savedListEntity)
                .portions(3)
                .build();
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Pizza", Category.MAIN_COURSE, List.of(), "Bake", 2, List.of());
        SavedList savedList = SavedList.builder()
                .listName(new ListName("Favourites"))
                .createdAt(Instant.now())
                .listDescription("List descriptions")
                .expectedPortions(4)
                .entries(Collections.emptyList())
                .build();
        when(recipeMapper.toDomain(recipeEntity)).thenReturn(recipe);
        when(savedListMapper.toDomainWithoutEntries(savedListEntity)).thenReturn(savedList);
        ListEntry domain = listEntryMapper.toDomain(entity);

        assertEquals(entryId, domain.getEntryId());
        assertEquals(recipe, domain.getRecipe());
        assertEquals(savedList, domain.getSavedRecipeList());
        assertEquals(3, domain.getPortions());
    }

    @Test
    void mapDomainToEntityCorrectly() {
        UUID entryId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Soup", Category.MAIN_COURSE, List.of(), "Boil", 1, List.of());
        SavedList savedList = SavedList.builder()
                .listName(new ListName("Dinner"))
                .createdAt(Instant.now())
                .listDescription("Dinner list description")
                .expectedPortions(3)
                .entries(Collections.emptyList())
                .build();
        ListEntry domain = ListEntry.builder()
                .entryId(entryId)
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(2)
                .build();
        RecipeEntity recipeEntity = new RecipeEntity();
        SavedListEntity savedListEntity = new SavedListEntity();
        when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
        ListEntryEntity entity = listEntryMapper.toEntity(domain, savedListEntity);

        assertEquals(entryId, entity.getEntryId());
        assertEquals(recipeEntity, entity.getRecipe());
        assertEquals(savedListEntity, entity.getSavedList());
        assertEquals(2, entity.getPortions());
    }

    @Test
    void throwExceptionWhenEntryIdIsNull() {
        ListEntryEntity entity = ListEntryEntity.builder()
                .entryId(null)
                .recipe(new RecipeEntity())
                .savedList(new SavedListEntity())
                .portions(1)
                .build();

        assertThrows(ListValidationException.class, () -> listEntryMapper.toDomain(entity));
    }

    @Test
    void throwExceptionWhenRecipeIsNull() {
        ListEntryEntity entity = ListEntryEntity.builder()
                .entryId(UUID.randomUUID())
                .recipe(null)
                .savedList(new SavedListEntity())
                .portions(1)
                .build();

        when(recipeMapper.toDomain(null)).thenThrow(new RecipeValidationException("Recipe cannot be null."));

        assertThrows(RecipeValidationException.class, () -> listEntryMapper.toDomain(entity));
    }
}