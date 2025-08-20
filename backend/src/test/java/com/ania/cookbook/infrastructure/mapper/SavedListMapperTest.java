package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedListMapperTest {
    @Mock
    private ListEntryMapper entryMapper;

    private SavedListMapper savedListMapper;

    @BeforeEach
    void setUp() {
        savedListMapper = new SavedListMapper(entryMapper);
    }
    @Test
    void mapEntityToDomainCorrectly() {
        UUID entryId = UUID.randomUUID();
        ListEntryEntity entryEntity = ListEntryEntity.builder()
                .entryId(entryId)
                .portions(2)
                .build();
        SavedListEntity entity = SavedListEntity.builder()
                .listName("Favourites")
                .createdAt(ZonedDateTime.of(2025, 8, 20, 14, 0, 0, 0, ZoneId.of("Europe/Warsaw")).toInstant())
                .listDescription("Test list description")
                .expectedPortions(4)
                .entries(List.of(entryEntity))
                .build();
        ListEntry entry = ListEntry.builder()
                .entryId(entryId)
                .portions(2)
                .recipe(mock(Recipe.class))
                .savedRecipeList(mock(SavedList.class))
                .build();
        when(entryMapper.toDomain(entryEntity)).thenReturn(entry);
        SavedList domain = savedListMapper.toDomain(entity);

        assertEquals(new ListName("Favourites"), domain.getListName());
        assertEquals(Instant.parse("2025-08-20T12:00:00Z"), domain.getCreatedAt());
        assertEquals("Test list description", domain.getListDescription());
        assertEquals(4, domain.getExpectedPortions());
        assertEquals(1, domain.getEntries().size());
        assertEquals(entry, domain.getEntries().getFirst());
    }

    @Test
    void mapDomainToEntityCorrectly() {
        UUID entryId = UUID.randomUUID();
        ListEntry entry = ListEntry.builder()
                .entryId(entryId)
                .portions(3)
                .recipe(mock(Recipe.class))
                .savedRecipeList(mock(SavedList.class))
                .build();
        ListEntryEntity entryEntity = ListEntryEntity.builder()
                .entryId(entryId)
                .portions(3)
                .build();
        SavedList domain = SavedList.builder()
                .listName(new ListName("Dinner"))
                .createdAt(ZonedDateTime.of(2025, 8, 20, 14, 0, 0, 0, ZoneId.of("Europe/Warsaw")).toInstant())
                .listDescription("Dinner list")
                .expectedPortions(2)
                .entries(List.of(entry))
                .build();
        when(entryMapper.toEntity(eq(entry), any(SavedListEntity.class))).thenAnswer(invocation -> {
            SavedListEntity savedListEntity = invocation.getArgument(1);
            entryEntity.setSavedList(savedListEntity);
            return entryEntity;
        });
        SavedListEntity entity = savedListMapper.toEntity(domain);

        assertEquals("Dinner", entity.getListName());
        assertEquals(Instant.parse("2025-08-20T12:00:00Z"), entity.getCreatedAt());
        assertEquals("Dinner list", entity.getListDescription());
        assertEquals(2, entity.getExpectedPortions());
        assertEquals(1, entity.getEntries().size());
        assertEquals(entryEntity, entity.getEntries().getFirst());
        assertEquals(entity, entity.getEntries().getFirst().getSavedList());
    }

    @Test
    void handleNullEntriesInSavedListEntity() {
        SavedListEntity entity = SavedListEntity.builder()
                .listName("EmptyList")
                .createdAt(Instant.parse("2025-08-20T12:00:00Z"))
                .listDescription("No entries")
                .expectedPortions(1)
                .entries(null)
                .build();
        SavedList domain = savedListMapper.toDomain(entity);

        assertEquals("EmptyList", domain.getListName().name());
        assertTrue(domain.getEntries().isEmpty());
    }

    @Test
    void handleEmptyEntriesInSavedListDomain() {
        SavedList domain = SavedList.builder()
                .listName(new ListName("EmptyList"))
                .createdAt(Instant.parse("2025-08-20T12:00:00Z"))
                .listDescription("No entries")
                .expectedPortions(1)
                .entries(Collections.emptyList())
                .build();
        SavedListEntity entity = savedListMapper.toEntity(domain);

        assertEquals("EmptyList", entity.getListName());
        assertTrue(entity.getEntries().isEmpty());
    }
}