package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.RecipeListEntryDomain;
import com.ania.cookbook.domain.model.SavedRecipeListDomain;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedRecipeListMapperTest {
    @Mock
    private RecipeListEntryMapper entryMapper;

    @InjectMocks
    private SavedRecipeListMapper savedRecipeListMapper;

//    @Test
//    void mapEntityToDomain() {
//        UUID entryId = UUID.randomUUID();
//        Instant createdAt = Instant.now();
//
//        RecipeListEntry entry = new RecipeListEntry();
//        entry.setEntryId(entryId);
//        SavedRecipeList entity = SavedRecipeList.builder()
//                .listName("My List")
//                .createdAt(createdAt)
//                .listDescription("Description")
//                .expectedPortions(4)
//                .entries(List.of(entry))
//                .build();
//        RecipeListEntryDomain entryDomain = RecipeListEntryDomain.builder()
//                .entryId(entryId)
//                .portions(4)
//                .recipe(mock(Recipe.class))
//                .savedRecipeList(mock(SavedRecipeListDomain.class))
//                .build();
//        when(entryMapper.toDomain(entry)).thenReturn(entryDomain);
//        SavedRecipeListDomain domain = savedRecipeListMapper.toDomain(entity);
//
//        assertEquals("My List", domain.getListName().name());
//        assertEquals("Description", domain.getListDescription());
//        assertEquals(4, domain.getExpectedPortions());
//        assertEquals(createdAt, domain.getCreatedAt());
//        assertEquals(1, domain.getEntries().size());
//        assertEquals(entryDomain, domain.getEntries().getFirst());
//        assertNotNull(domain.getEntries());
//
//    }

//    @Test
//    void mapEntityWithEmptyEntriesToDomain() {
//        SavedRecipeList entity = SavedRecipeList.builder()
//                .listName("Empty List")
//                .createdAt(Instant.now())
//                .listDescription("No entries")
//                .expectedPortions(1)
//                .entries(Collections.emptyList())
//                .build();
//        SavedRecipeListDomain domain = savedRecipeListMapper.toDomain(entity);
//
//        assertEquals("Empty List", domain.getListName().name());
//        assertTrue(domain.getEntries().isEmpty());
//    }

//    @Test
//    void mapEntityWithNullEntriesToDomain() {
//        SavedRecipeList entity = SavedRecipeList.builder()
//                .listName("Null List")
//                .createdAt(Instant.now())
//                .listDescription("Null entries")
//                .expectedPortions(1)
//                .entries(null)
//                .build();
//        SavedRecipeListDomain domain = savedRecipeListMapper.toDomain(entity);
//
//        assertEquals("Null List", domain.getListName().name());
//        assertTrue(domain.getEntries().isEmpty());
//    }

    @Test
    void mapDomainToEntity() {
        RecipeListEntryDomain entryDomain = RecipeListEntryDomain.builder()
                .entryId(UUID.randomUUID())
                .portions(4)
                .recipe(mock(Recipe.class))
                .savedRecipeList(mock(SavedRecipeListDomain.class))
                .build();
        SavedRecipeListDomain domain = SavedRecipeListDomain.builder()
                .listName(new ListName("My List"))
                .createdAt(Instant.now())
                .listDescription("Description")
                .expectedPortions(4)
                .entries(List.of(entryDomain))
                .build();
        RecipeListEntry entry = new RecipeListEntry();
        entry.setEntryId(entryDomain.getEntryId());
        SavedRecipeList entity = savedRecipeListMapper.toEntity(domain, List.of(entry));
        entity.getEntries().forEach(e -> e.setSavedList(entity));

        assertEquals("My List", entity.getListName());
        assertEquals("Description", entity.getListDescription());
        assertEquals(4, entity.getExpectedPortions());
        assertEquals(1, entity.getEntries().size());
        assertEquals(entry, entity.getEntries().getFirst());
        assertEquals(entity, entity.getEntries().getFirst().getSavedList());
    }

    @Test
    void mapDomainWithEmptyEntriesToEntity() {
        SavedRecipeListDomain domain = SavedRecipeListDomain.builder()
                .listName(new ListName("Empty Domain"))
                .createdAt(Instant.now())
                .listDescription("No entries")
                .expectedPortions(1)
                .entries(Collections.emptyList())
                .build();
        SavedRecipeList entity = savedRecipeListMapper.toEntity(domain, Collections.emptyList());

        assertEquals("Empty Domain", entity.getListName());
        assertTrue(entity.getEntries().isEmpty());
    }
}