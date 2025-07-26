package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.mapper.RecipeListEntryMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.ania.cookbook.web.recipe.RecipeListEntryResponse;
import com.ania.cookbook.web.recipe.RecipeListRequest;
import com.ania.cookbook.web.recipe.RecipeListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeListController.class)
@Import(RecipeListTestConfig.class)
class RecipeListControllerTest {
    private static final String LIST_NAME = "MyList";
    private static final UUID RECIPE_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID ENTRY_ID = UUID.randomUUID();
    private static final int PORTIONS = 6;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeManagementService recipeManagementService;

    @Autowired
    private RecipeListEntryMapper entryMapper;

    private RecipeListRequest buildListRequestWithName() {
        return RecipeListRequest.builder().listName("").build();
    }

    private RecipeListRequest buildAddRecipeRequest() {
        return RecipeListRequest.builder().recipeId(RECIPE_ID).portions(PORTIONS).build();
    }

    @Test
    void createRecipeList() throws Exception {
        String description = "Weekend shopping";
        int expectingPortions = 4;
        RecipeListRequest request = RecipeListRequest.builder()
                .listName(LIST_NAME)
                .listDescription(description)
                .portions(expectingPortions)
                .build();
        String json = objectMapper.writeValueAsString(request);
        doNothing().when(recipeManagementService).createRecipeList(any(ListName.class), eq(description),eq(expectingPortions));

        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());
        verify(recipeManagementService, times(1))
                .createRecipeList(eq(new ListName(LIST_NAME)),eq(description),eq(expectingPortions));
    }

    @Test
    void createRecipeListWhenNameIsBlank() throws Exception {
        RecipeListRequest request = buildListRequestWithName();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("List name cannot be null or empty.")));
    }

    @Test
    void addRecipeToList() throws Exception {
        RecipeListRequest request = buildAddRecipeRequest();
        String json = objectMapper.writeValueAsString(request);
        RecipeListEntry entry = mock(RecipeListEntry.class);
        Recipe recipe = Recipe.builder()
                .recipeId(RECIPE_ID)
                .recipeName("Test Recipe")
                .build();
        ReadRecipeResponse readRecipeResponse = ReadRecipeResponse.from(recipe);
        RecipeListEntryResponse response = RecipeListEntryResponse.builder()
                .entryId(ENTRY_ID)
                .portions(PORTIONS)
                .recipe(readRecipeResponse)
                .build();
        when(recipeManagementService.addRecipeToList(eq(RECIPE_ID), eq(new ListName(LIST_NAME)), eq(PORTIONS)))
                .thenReturn(entry);
        when(entryMapper.toResponse(entry)).thenReturn(response);

        mockMvc.perform(post("/api/lists/{listName}/recipes", LIST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(recipeManagementService).addRecipeToList(eq(RECIPE_ID), eq(new ListName(LIST_NAME)), eq(PORTIONS));
        verify(entryMapper).toResponse(entry);
    }

    @Test
    void addRecipeToListWhenListNameIsBlank() throws Exception {
        RecipeListRequest request = buildAddRecipeRequest();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/lists/{listName}/recipes", " ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("List name cannot be null or empty.")));
    }

    @Test
    void getRecipesList() throws Exception {
        Recipe recipe = Recipe.builder().recipeId(RECIPE_ID).recipeName("Recipe").build();
        ReadRecipeResponse readRecipeResponse = ReadRecipeResponse.from(recipe);
        RecipeListEntryResponse entryResponse = RecipeListEntryResponse.builder()
                .entryId(ENTRY_ID)
                .portions(PORTIONS)
                .recipe(readRecipeResponse)
                .build();
        List<RecipeListEntryResponse> entryResponses = List.of(entryResponse);
        RecipeListResponse response = RecipeListResponse.builder()
                .listName(new ListName(LIST_NAME))
                .recipes(entryResponses)
                .build();
        when(recipeManagementService.getRecipeListResponse(eq(new ListName(LIST_NAME))))
                .thenReturn(response);
        String expectedJson = objectMapper.writeValueAsString(response);

        mockMvc.perform(get("/api/lists/{listName}", LIST_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectedJson));

        verify(recipeManagementService).getRecipeListResponse(eq(new ListName(LIST_NAME)));
    }

    @Test
    void getRecipesListWhenNotFound() throws Exception {
        String errorMessage = "Recipe list with the given name does not exist.";

        when(recipeManagementService.getRecipeListResponse(any()))
                .thenThrow(new ListNotFoundException(errorMessage));

        mockMvc.perform(get("/api/lists/NonExistentList")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(Matchers.containsString(errorMessage)));
    }

    @Test
    void getRecipesListWhenListNameIsBlank() throws Exception {
        mockMvc.perform(get("/api/lists/ ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("List name cannot be null or empty.")));
    }

    @Test
    void removeRecipeEntryFromList() throws Exception {
        doNothing().when(recipeManagementService)
                .removeRecipeFromList(eq(ENTRY_ID), eq(new ListName(LIST_NAME)));

        mockMvc.perform(delete("/api/lists/{listName}/entries/{entryId}", LIST_NAME, ENTRY_ID))
                .andExpect(status().isNoContent());

        verify(recipeManagementService).removeRecipeFromList(eq(ENTRY_ID), eq(new ListName(LIST_NAME)));
    }

    @Test
    void clearRecipeList() throws Exception {
        when(recipeManagementService.clearRecipeList(any(ListName.class), eq(true))).thenReturn(true);

        mockMvc.perform(delete("/api/lists/{listName}/clear", LIST_NAME)
                        .param("confirm", "true"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("true"));
        verify(recipeManagementService).clearRecipeList(eq(new ListName(LIST_NAME)), eq(true));
    }

    @Test
    void deleteRecipeList() throws Exception {
        doNothing().when(recipeManagementService).deleteRecipeList(any());

        mockMvc.perform(delete("/api/lists/{listName}", LIST_NAME))
                .andExpect(status().isNoContent());
        verify(recipeManagementService).deleteRecipeList(eq(new ListName(LIST_NAME)));
    }

    @Test
    void generateShoppingList() throws Exception {
        Map<String, Float> shoppingList = Map.of("Milk", 500F);
        when(recipeManagementService.generateShoppingList(any())).thenReturn(shoppingList);

        mockMvc.perform(get("/api/lists/{listName}/shopping", LIST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Milk", is(500.0)));
        verify(recipeManagementService).generateShoppingList(eq(new ListName(LIST_NAME)));
    }

    @Test
    void getAllLists() throws Exception {
        List<ListName> mockLists = List.of(new ListName("Breakfast"), new ListName("Dinner"));
        when(recipeManagementService.getAllLists()).thenReturn(mockLists);

        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("Breakfast")))
                .andExpect(jsonPath("$[1]", is("Dinner")));
    }
}