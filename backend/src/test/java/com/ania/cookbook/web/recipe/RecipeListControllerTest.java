package com.ania.cookbook.web.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.controllers.recipe.RecipeListController;
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
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeManagementService recipeManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createRecipeList() throws Exception {
        RecipeListRequest request = RecipeListRequest.builder()
                .listName("MyList")
                .build();
        String json = objectMapper.writeValueAsString(request);

        doNothing().when(recipeManagementService).createRecipeList(any(ListName.class));

        mockMvc.perform(post("/api/recipes/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(recipeManagementService, times(1))
                .createRecipeList(eq(new ListName("MyList")));
    }

    @Test
    public void createRecipeListWhenNameIsBlank() throws Exception {
        RecipeListRequest request = RecipeListRequest.builder()
                .listName("")
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recipes/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("List name cannot be null or empty.")));
    }

    @Test
    public void addRecipeToList() throws Exception {
        UUID recipeId = UUID.randomUUID();
        RecipeListRequest request = RecipeListRequest.builder()
                .recipeId(recipeId)
                .build();
        String json = objectMapper.writeValueAsString(request);

        doNothing().when(recipeManagementService)
                .addRecipeToList(any(UUID.class), any(ListName.class));

        mockMvc.perform(post("/api/recipes/lists/MyList/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(recipeManagementService, times(1))
                .addRecipeToList(eq(recipeId), eq(new ListName("MyList")));
    }
    @Test
    public void addRecipeToListWhenListNameIsBlank() throws Exception {
        RecipeListRequest request = RecipeListRequest.builder()
                .recipeId(UUID.randomUUID())
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recipes/lists/{listName}/recipes", " ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("List name cannot be null or empty.")));
    }

    @Test
    public void saveRecipesList() throws Exception {
        doNothing().when(recipeManagementService).saveRecipesList(any(ListName.class));

        mockMvc.perform(post("/api/recipes/lists/MyList/save"))
                .andExpect(status().isOk());

        verify(recipeManagementService, times(1))
                .saveRecipesList(eq(new ListName("MyList")));
    }

    @Test
    public void getRecipesList() throws Exception {
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Recipe")
                .build();

        when(recipeManagementService.getRecipesList(any(ListName.class)))
                .thenReturn(Collections.singletonList(recipe));

        mockMvc.perform(get("/api/recipes/lists/MyList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listName.name", is("MyList")))
                .andExpect(jsonPath("$.recipes", hasSize(1)));

        verify(recipeManagementService, times(1))
                .getRecipesList(eq(new ListName("MyList")));
    }

    @Test
    public void getRecipesListWhenNotFound() throws Exception {
        when(recipeManagementService.getRecipesList(any(ListName.class)))
                .thenThrow(new ListNotFoundException("Recipe list with the given name does not exist."));

        mockMvc.perform(get("/api/recipes/lists/NonExistentList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recipe list with the given name does not exist."));
    }

    @Test
    public void removeRecipeFromList() throws Exception {
        UUID recipeId = UUID.randomUUID();

        doNothing().when(recipeManagementService)
                .removeRecipeFromList(any(UUID.class), any(ListName.class));

        mockMvc.perform(delete("/api/recipes/lists/MyList/recipes/{recipeId}", recipeId))
                .andExpect(status().isOk());

        verify(recipeManagementService, times(1))
                .removeRecipeFromList(eq(recipeId), eq(new ListName("MyList")));
    }

    @Test
    public void clearRecipeList() throws Exception {
        when(recipeManagementService.clearRecipeList(any(ListName.class), eq(true))).thenReturn(true);

        mockMvc.perform(delete("/api/recipes/lists/MyList/clear")
                        .param("confirm", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(recipeManagementService, times(1))
                .clearRecipeList(eq(new ListName("MyList")), eq(true));
    }

    @Test
    public void deleteRecipeList() throws Exception {
        doNothing().when(recipeManagementService).deleteRecipeList(any(ListName.class));

        mockMvc.perform(delete("/api/recipes/lists/MyList"))
                .andExpect(status().isOk());

        verify(recipeManagementService, times(1))
                .deleteRecipeList(eq(new ListName("MyList")));
    }

    @Test
    public void generateShoppingList() throws Exception {
        Map<String, Float> shoppingList = new HashMap<>();
        shoppingList.put("Milk", 500F);

        when(recipeManagementService.generateShoppingList(any(ListName.class))).thenReturn(shoppingList);

        mockMvc.perform(get("/api/recipes/lists/MyList/shopping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Milk", is(500.0)));

        verify(recipeManagementService, times(1))
                .generateShoppingList(eq(new ListName("MyList")));
    }
}