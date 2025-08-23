package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.list.ListResponse;
import com.ania.cookbook.web.recipe.RecipeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListResponseMapperTest {
    @Mock
    private ListEntryResponseMapper entryResponseMapper;

    private ListResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ListResponseMapper(entryResponseMapper);
    }

    @Test
    void mapSavedListToListResponseCorrectly() {
        UUID entryId = UUID.randomUUID();
        ListName listName = new ListName("MyRecipes");
        ListEntryResponse entryResponse = ListEntryResponse.builder()
                .entryId(entryId)
                .portions(2)
                .recipe(new RecipeResponse(
                        UUID.randomUUID(),
                        "Pancakes",
                        Category.DESSERT,
                        List.of(),
                        "Mix and fry",
                        4,
                        List.of("Sweet")))
                .build();
        ListEntry entry = ListEntry.builder()
                .entryId(entryId)
                .recipe(mock(Recipe.class))
                .savedRecipeList(mock(SavedList.class))
                .portions(2)
                .build();
        SavedList savedList = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .listDescription("Weekend breakfast ideas")
                .expectedPortions(4)
                .entries(List.of(entry))
                .build();
        when(entryResponseMapper.toResponseList(savedList.getEntries())).thenReturn(List.of(entryResponse));
        ListResponse response = mapper.from(savedList);

        assertEquals(listName, response.getListName());
        assertEquals("Weekend breakfast ideas", response.getListDescription());
        assertEquals(4, response.getExpectedPortions());
        assertEquals(1, response.getRecipes().size());
        assertEquals(entryResponse, response.getRecipes().getFirst());
    }

    @Test
    void mapSavedListWithEmptyEntries() {
        ListName listName = new ListName("EmptyList");
        SavedList savedList = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .listDescription("No recipes yet")
                .expectedPortions(1)
                .entries(List.of())
                .build();
        when(entryResponseMapper.toResponseList(List.of())).thenReturn(List.of());
        ListResponse response = mapper.from(savedList);

        assertEquals(listName, response.getListName());
        assertEquals("No recipes yet", response.getListDescription());
        assertEquals(1, response.getExpectedPortions());
        assertTrue(response.getRecipes().isEmpty());
    }
}