package com.ania.cookbook.web.controllers.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.application.services.implementations.list.ListService;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.mapper.ListEntryResponseMapper;
import com.ania.cookbook.infrastructure.mapper.ListResponseMapper;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.list.ListRequest;
import com.ania.cookbook.web.list.ListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListControllerTest {
    @Mock
    private ListService listService;

    @Mock
    private ListEntryResponseMapper entryResponseMapper;

    @Mock
    private ListResponseMapper listResponseMapper;

    @InjectMocks
    private ListController controller;

    @Test
    void createRecipeListSuccessfully() {
        ListRequest request = ListRequest.builder()
                .listName("Dinner")
                .listDescription("Weekend list")
                .portions(4)
                .build();
        ResponseEntity<Void> response = controller.createRecipeList(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(listService).createRecipeList(
                new ListName("Dinner"),
                "Weekend list",
                4);
    }

    @Test
    void addRecipeToListAndReturnResponse() {
        UUID recipeId = UUID.randomUUID();
        String listName = "Dinner";
        ListRequest request = ListRequest.builder()
                .recipeId(recipeId)
                .portions(2)
                .build();
        Recipe mockRecipe = mock(Recipe.class);
        SavedList mockSavedList = mock(SavedList.class);
        ListEntry entry = ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(mockRecipe)
                .savedRecipeList(mockSavedList)
                .portions(2)
                .build();
        ListEntryResponse expectedResponse = ListEntryResponse.builder()
                .entryId(entry.getEntryId())
                .portions(2)
                .build();
        when(listService.addRecipeToList(recipeId, new ListName(listName), 2)).thenReturn(entry);
        when(entryResponseMapper.toResponse(entry)).thenReturn(expectedResponse);
        ResponseEntity<ListEntryResponse> response = controller.addRecipeToList(listName, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void returnAllListNames() {
        List<ListName> domainNames = List.of(new ListName("Lunch"), new ListName("Dinner"));
        when(listService.getAllLists()).thenReturn(domainNames);
        ResponseEntity<List<String>> response = controller.getAllLists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("Lunch", "Dinner"), response.getBody());
    }

    @Test
    void returnRecipesList() {
        SavedList savedList = SavedList.builder()
                .listName(new ListName("Dinner"))
                .expectedPortions(2)
                .entries(List.of())
                .build();
        ListResponse expected = ListResponse.builder()
                .listName(new ListName("Dinner"))
                .expectedPortions(2)
                .recipes(List.of())
                .build();
        when(listService.getRecipesList(new ListName("Dinner"))).thenReturn(savedList);
        when(listResponseMapper.from(savedList)).thenReturn(expected);
        ResponseEntity<ListResponse> response = controller.getRecipesList("Dinner");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void throwExceptionWhenListNameIsBlank() {
        assertThrows(ListValidationException.class, () -> controller.getRecipesList(" "));
    }

    @Test
    void removeRecipeFromList() {
        UUID entryId = UUID.randomUUID();
        ResponseEntity<Void> response = controller.removeRecipeFromList("Dinner", entryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(listService).removeRecipeFromList(entryId, new ListName("Dinner"));
    }

    @Test
    void clearRecipeListWhenConfirmed() {
        when(listService.clearRecipeList(new ListName("Dinner"), true)).thenReturn(true);
        ResponseEntity<Boolean> response = controller.clearRecipeList("Dinner", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void notClearRecipeListWhenNotConfirmed() {
        when(listService.clearRecipeList(new ListName("Dinner"), false)).thenReturn(false);
        ResponseEntity<Boolean> response = controller.clearRecipeList("Dinner", false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void deleteRecipeList() {
        ResponseEntity<Void> response = controller.deleteRecipeList("Dinner");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(listService).deleteRecipeList(new ListName("Dinner"));
    }

    @Test
    void generateShoppingList() {
        Map<String, Float> expected = Map.of("Mąka", 500f, "Eggs", 3f);
        when(listService.generateShoppingList(new ListName("Dinner"))).thenReturn(expected);
        ResponseEntity<Map<String, Float>> response = controller.generateShoppingList("Dinner");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void returnEmptyShoppingListWhenNoEntries() {
        when(listService.generateShoppingList(new ListName("Dinner"))).thenReturn(Map.of());
        ResponseEntity<Map<String, Float>> response = controller.generateShoppingList("Dinner");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void throwListNotFoundExceptionWhenGettingNonexistentList() {
        String listName = "Not existing";
        when(listService.getRecipesList(new ListName(listName)))
                .thenThrow(new ListNotFoundException("List not found"));
        ListNotFoundException ex = assertThrows(ListNotFoundException.class, () ->
                controller.getRecipesList(listName));

        assertEquals("List not found", ex.getMessage());
    }

    @Test
    void throwRecipeValidationExceptionWhenRecipeNotFound() {
        UUID recipeId = UUID.randomUUID();
        String listName = "Dinner";
        ListRequest request = ListRequest.builder()
                .recipeId(recipeId)
                .portions(2)
                .build();
        when(listService.addRecipeToList(recipeId, new ListName(listName), 2))
                .thenThrow(new RecipeValidationException("Recipe not found"));

        RecipeValidationException ex = assertThrows(RecipeValidationException.class, () ->
                controller.addRecipeToList(listName, request));
        assertEquals("Recipe not found", ex.getMessage());
    }

    @Test
    void throwListValidationExceptionWhenEntryDoesNotBelongToList() {
        UUID entryId = UUID.randomUUID();
        String listName = "Dinner";

        doThrow(new ListValidationException("Entry does not belong to the specified list"))
                .when(listService).removeRecipeFromList(entryId, new ListName(listName));
        ListValidationException ex = assertThrows(ListValidationException.class, () ->
                controller.removeRecipeFromList(listName, entryId));
        assertEquals("Entry does not belong to the specified list", ex.getMessage());
    }

    @Test
    void throwListNotFoundExceptionWhenClearingEmptyList() {
        String listName = "Empty";
        when(listService.clearRecipeList(new ListName(listName), true))
                .thenThrow(new ListNotFoundException("List is empty or not found"));

        ListNotFoundException ex = assertThrows(ListNotFoundException.class, () ->
                controller.clearRecipeList(listName, true));
        assertEquals("List is empty or not found", ex.getMessage());
    }

    @Test
    void throwListNotFoundExceptionWhenDeletingNonexistentList() {
        String listName = "Not existing";

        doThrow(new ListNotFoundException("List not found"))
                .when(listService).deleteRecipeList(new ListName(listName));
        ListNotFoundException ex = assertThrows(ListNotFoundException.class, () ->
                controller.deleteRecipeList(listName));
        assertEquals("List not found", ex.getMessage());
    }

    @Test
    void throwListNotFoundExceptionWhenGeneratingShoppingListForMissingList() {
        String listName = "List";
        when(listService.generateShoppingList(new ListName(listName)))
                .thenThrow(new ListNotFoundException("List not found"));

        ListNotFoundException ex = assertThrows(ListNotFoundException.class, () ->
                controller.generateShoppingList(listName));
        assertEquals("List not found", ex.getMessage());
    }
}