package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.web.list.ListEntryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListEntryResponseMapperTest {
    private ListEntryResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ListEntryResponseMapper();
    }

    @Test
    void mapListEntryToResponseCorrectly() {
        UUID entryId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(
                recipeId,
                "Spaghetti",
                Category.MAIN_COURSE,
                List.of(),
                "Boil pasta and add sauce",
                2,
                List.of("Italian", "Quick"));
        SavedList savedList = SavedList.builder()
                .listName(new ListName("MyList"))
                .createdAt(Instant.now())
                .listDescription("desc")
                .expectedPortions(2)
                .entries(List.of())
                .build();
        ListEntry entry = ListEntry.builder()
                .entryId(entryId)
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(1)
                .build();
        ListEntryResponse response = mapper.toResponse(entry);

        assertEquals(entryId, response.getEntryId());
        assertEquals(1, response.getPortions());
        assertNotNull(response.getRecipe());
        assertEquals(recipeId, response.getRecipe().recipeId());
        assertEquals("Spaghetti", response.getRecipe().recipeName());
        assertEquals(Category.MAIN_COURSE, response.getRecipe().category());
        assertEquals("Boil pasta and add sauce", response.getRecipe().instructions());
        assertEquals(2, response.getRecipe().numberOfServings());
        assertEquals(List.of("Italian", "Quick"), response.getRecipe().tags());
        assertTrue(response.getRecipe().ingredients().isEmpty());
    }

    @Test
    void throwExceptionWhenEntryIsNull() {
        assertThrows(ListValidationException.class, () -> mapper.toResponse(null));
    }

    @Test
    void mapEmptyListToEmptyResponseList() {
        List<ListEntryResponse> responses = mapper.toResponseList(List.of());
        assertTrue(responses.isEmpty());
    }

    @Test
    void mapMultipleEntriesToResponseList() {
        UUID recipeId1 = UUID.randomUUID();
        UUID recipeId2 = UUID.randomUUID();
        Recipe recipe1 = Recipe.newRecipe(recipeId1, "Soup", Category.SNACK, List.of(), "Boil", 1, List.of());
        Recipe recipe2 = Recipe.newRecipe(recipeId2, "Cake", Category.DESSERT, List.of(), "Bake", 6, List.of("Sweet"));
        SavedList savedList = SavedList.builder()
                .listName(new ListName("Party"))
                .createdAt(Instant.now())
                .listDescription("desc")
                .expectedPortions(5)
                .entries(List.of())
                .build();
        ListEntry entry1 = ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe1)
                .savedRecipeList(savedList)
                .portions(2)
                .build();
        ListEntry entry2 = ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe2)
                .savedRecipeList(savedList)
                .portions(3)
                .build();
        List<ListEntryResponse> responses = mapper.toResponseList(List.of(entry1, entry2));

        assertEquals(2, responses.size());
        assertEquals("Soup", responses.get(0).getRecipe().recipeName());
        assertEquals("Cake", responses.get(1).getRecipe().recipeName());
    }
}