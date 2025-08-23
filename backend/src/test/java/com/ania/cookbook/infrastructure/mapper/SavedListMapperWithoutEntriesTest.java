package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SavedListMapperWithoutEntriesTest {
    private SavedListMapperWithoutEntries mapper;

    @BeforeEach
    void setUp() {
        mapper = new SavedListMapperWithoutEntries();
    }

    @Test
    void shouldMapEntityToDomainWithoutEntriesCorrectly() {
        SavedListEntity entity = SavedListEntity.builder()
                .listName("QuickMeals")
                .createdAt(Instant.parse("2025-08-20T12:00:00Z"))
                .listDescription("Fast and easy recipes")
                .expectedPortions(5)
                .entries(List.of(mock(ListEntryEntity.class)))
                .build();
        SavedList domain = mapper.toDomainWithoutEntries(entity);

        assertEquals(new ListName("QuickMeals"), domain.getListName());
        assertEquals(Instant.parse("2025-08-20T12:00:00Z"), domain.getCreatedAt());
        assertEquals("Fast and easy recipes", domain.getListDescription());
        assertEquals(5, domain.getExpectedPortions());
        assertTrue(domain.getEntries().isEmpty());
    }

    @Test
    void shouldThrowWhenListNameIsNull() {
        SavedListEntity entity = SavedListEntity.builder()
                .listName(null)
                .createdAt(Instant.now())
                .listDescription("desc")
                .expectedPortions(1)
                .entries(null)
                .build();

        assertThrows(ListValidationException.class, () -> mapper.toDomainWithoutEntries(entity));
    }

    @Test
    void shouldThrowWhenExpectedPortionsIsZero() {
        SavedListEntity entity = SavedListEntity.builder()
                .listName("TestList")
                .createdAt(Instant.now())
                .listDescription("desc")
                .expectedPortions(0)
                .entries(null)
                .build();

        assertThrows(ListValidationException.class, () -> mapper.toDomainWithoutEntries(entity));
    }

    @Test
    void shouldUseCurrentTimeWhenCreatedAtIsNull() {
        SavedListEntity entity = SavedListEntity.builder()
                .listName("TestList")
                .createdAt(null)
                .listDescription("desc")
                .expectedPortions(1)
                .entries(null)
                .build();

        SavedList domain = mapper.toDomainWithoutEntries(entity);
        assertNotNull(domain.getCreatedAt());
    }
}