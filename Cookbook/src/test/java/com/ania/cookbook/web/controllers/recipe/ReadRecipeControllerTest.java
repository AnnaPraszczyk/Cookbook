package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.ReadRecipeService;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(ReadRecipeController.class)
class ReadRecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReadRecipeService readRecipeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID recipeId = UUID.randomUUID();

    private Recipe createSampleRecipe() {
        return Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Pancakes")
                .category(Category.DESSERT)
                .ingredients(List.of())
                .instructions("Mix everything and bake.")
                .numberOfServings(4)
                .tags(Arrays.asList("Breakfast", "Sweet"))
                .build();
    }

    @Test
    public void getRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(readRecipeService.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));

        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Pancakes"))
                .andExpect(jsonPath("$.category").value("DESSERT"))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.tags[1]").value("Sweet"));
    }

    @Test
    public void getRecipeById_NotFound() throws Exception {
        when(readRecipeService.findRecipeById(recipeId))
                .thenThrow(new RecipeNotFoundException("Unable to find the recipe because it does not exist."));

        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Unable to find the recipe because it does not exist."));
    }

    @Test
    public void existsRecipeById() throws Exception {
        when(readRecipeService.existsRecipeById(recipeId)).thenReturn(true);

        mockMvc.perform(get("/api/recipes/{id}/exists", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getRecipesByName() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(readRecipeService.findRecipeByName("Pancakes")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byName")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pancakes"));
    }

    @Test
    public void existsRecipeByName() throws Exception {
        when(readRecipeService.existsRecipeByName("Pancakes")).thenReturn(true);

        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getRecipesByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(readRecipeService.findRecipeByCategory(Category.DESSERT)).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DESSERT"));
    }

    @Test
    public void getRecipesByTag() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(readRecipeService.findRecipeByTag("Breakfast")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byTag")
                        .param("tag", "Breakfast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags", hasSize(2)))
                .andExpect(jsonPath("$[0].tags[0]").value("Breakfast"));
    }

    @Test
    public void searchRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("recipeId", recipeId.toString());

        when(readRecipeService.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));

        mockMvc.perform(post("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()));
    }

    @Test
    public void searchRecipeByName() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("recipeName", "Pancakes");

        when(readRecipeService.findRecipeByName("Pancakes")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(post("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pancakes"));
    }

    @Test
    public void searchRecipeByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("category", "dessert");

        when(readRecipeService.findRecipeByCategory(Category.DESSERT)).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(post("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DESSERT"));
    }

    @Test
    public void searchRecipeByTag() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("tag", "Breakfast");

        when(readRecipeService.findRecipeByTag("Breakfast")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(post("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags[0]").value("Breakfast"));
    }

    @Test
    public void searchRecipeWithoutParametersBadRequest() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();

        mockMvc.perform(post("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("At least one search parameter must be provided."));
    }
}