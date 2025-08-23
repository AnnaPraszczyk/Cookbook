package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import com.ania.cookbook.infrastructure.persistence.list.ListEntryRepository;
import com.ania.cookbook.infrastructure.persistence.list.SavedListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListEntryMapperTest {
    @Mock
    private RecipeMapper recipeMapper;
    @Mock
    private SavedListMapperWithoutEntries savedListMapper;
    @Mock
    private SavedListRepository savedListRepository;
    @Mock
    private ListEntryRepository listEntryRepository;

    private ListEntryMapper listEntryMapper;

    @BeforeEach
    void setUp() {
        listEntryMapper = new ListEntryMapper(recipeMapper, savedListMapper, savedListRepository, listEntryRepository );}

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
    void mapDomainToEntityWithLookupCorrectly() {
        UUID entryId = UUID.randomUUID();
        String listName = "LunchList";
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Salad", Category.MAIN_COURSE, List.of(), "Mix", 1, List.of());
        SavedList savedList = SavedList.builder()
                .listName(new ListName(listName))
                .createdAt(Instant.now())
                .listDescription("Healthy lunch")
                .expectedPortions(2)
                .entries(Collections.emptyList())
                .build();
        ListEntry domain = ListEntry.builder()
                .entryId(entryId)
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(1)
                .build();
        SavedListEntity savedListEntity = new SavedListEntity();
        RecipeEntity recipeEntity = new RecipeEntity();
        when(savedListRepository.findByListName(listName)).thenReturn(Optional.of(savedListEntity));
        when(listEntryRepository.existsById(entryId)).thenReturn(false);
        when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
        ListEntryEntity entity = listEntryMapper.toEntityWithLookup(domain);

        assertEquals(recipeEntity, entity.getRecipe());
        assertEquals(savedListEntity, entity.getSavedList());
        assertEquals(1, entity.getPortions());
    }

    @Test
    void shouldUpdateExistingEntityWhenEntryIdExists() {
        UUID entryId = UUID.randomUUID();
        String listName = "WeeklyMenu";
        Recipe recipe = Recipe.newRecipe(UUID.randomUUID(), "Risotto", Category.MAIN_COURSE, List.of(), "Stir", 2, List.of());
        SavedList savedList = SavedList.builder()
                .listName(new ListName(listName))
                .createdAt(Instant.now())
                .listDescription("Weekly meals")
                .expectedPortions(4)
                .entries(Collections.emptyList())
                .build();

        ListEntry domain = ListEntry.builder()
                .entryId(entryId)
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(3)
                .build();
        SavedListEntity savedListEntity = new SavedListEntity();
        RecipeEntity updatedRecipeEntity = new RecipeEntity();
        ListEntryEntity existingEntity = ListEntryEntity.builder()
                .entryId(entryId)
                .recipe(new RecipeEntity())
                .savedList(new SavedListEntity())
                .portions(1)
                .build();
        when(savedListRepository.findByListName(listName)).thenReturn(Optional.of(savedListEntity));
        when(listEntryRepository.existsById(entryId)).thenReturn(true);
        when(listEntryRepository.findById(entryId)).thenReturn(Optional.of(existingEntity));
        when(recipeMapper.toEntity(recipe)).thenReturn(updatedRecipeEntity);
        ListEntryEntity result = listEntryMapper.toEntityWithLookup(domain);

        assertEquals(entryId, result.getEntryId());
        assertEquals(updatedRecipeEntity, result.getRecipe());
        assertEquals(savedListEntity, result.getSavedList());
        assertEquals(3, result.getPortions());
    }
}