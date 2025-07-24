package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.recipe.RecipeListRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeListController.class)
class RecipeListControllerTest {
    private static final String LIST_NAME = "MyList";
    private static final UUID RECIPE_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID ENTRY_ID = UUID.randomUUID();
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeManagementService recipeManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    private RecipeListRequest buildListRequestWithName(String name) {
        return RecipeListRequest.builder().listName(name).build();
    }

    private RecipeListRequest buildAddRecipeRequest() {
        return RecipeListRequest.builder().recipeId(RECIPE_ID).build();
    }

    @Test
    void createRecipeList() throws Exception {
        String description = "Weekend shopping";
        RecipeListRequest request = RecipeListRequest.builder()
                .listName(LIST_NAME)
                .listDescription(description)
                .build();
        String json = objectMapper.writeValueAsString(request);
        doNothing().when(recipeManagementService).createRecipeList(any(ListName.class), eq(description));

        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());
        verify(recipeManagementService, times(1))
                .createRecipeList(eq(new ListName(LIST_NAME)),eq(description));
    }

    @Test
    void createRecipeListWhenNameIsBlank() throws Exception {
        RecipeListRequest request = buildListRequestWithName("");
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
        doNothing().when(recipeManagementService)
                .addRecipeToList(any(UUID.class), any(ListName.class));

        mockMvc.perform(post("/api/lists/{listName}/recipes", LIST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());
        verify(recipeManagementService).addRecipeToList(eq(RECIPE_ID), eq(new ListName(LIST_NAME)));
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
        when(recipeManagementService.getRecipesList(any()))
                .thenReturn(List.of(recipe));

        mockMvc.perform(get("/api/lists/{listName}", LIST_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.listName.name", is(LIST_NAME)))
                        .andExpect(jsonPath("$.recipes", hasSize(1)));
        verify(recipeManagementService).getRecipesList(eq(new ListName(LIST_NAME)));
    }

    @Test
    void getRecipesListWhenNotFound() throws Exception {
        String errorMessage = "Recipe list with the given name does not exist.";

        when(recipeManagementService.getRecipesList(any()))
                .thenThrow(new ListNotFoundException(errorMessage));

        mockMvc.perform(get("/api/lists/NonExistentList")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(errorMessage));
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