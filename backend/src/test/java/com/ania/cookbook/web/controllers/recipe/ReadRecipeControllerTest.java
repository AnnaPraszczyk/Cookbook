package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@WebMvcTest(ReadRecipeController.class)
@AutoConfigureMockMvc
class ReadRecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindRecipeUseCase finder;


    @Autowired
    private ObjectMapper objectMapper;

    private final UUID recipeId = UUID.randomUUID();
    private final List<Ingredient> ingredients = List.of(
            Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("flour")), 200f, Unit.G),
            Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("milk")), 300f, Unit.G)
    );

    private Recipe createSampleRecipe() {
        return Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Pancakes")
                .category(Category.DESSERT)
                .ingredients(ingredients)
                .instructions("Mix everything and bake.")
                .numberOfServings(4)
                .tags(Arrays.asList("Breakfast", "Sweet"))
                .build();
    }

    @Test
    public void getRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(finder.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));

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
        when(finder.findRecipeById(recipeId))
                .thenThrow(new RecipeNotFoundException("Unable to find the recipe because it does not exist."));

        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Unable to find the recipe because it does not exist."));
    }

    @Test
    public void existsRecipeById() throws Exception {
        when(finder.existsRecipeById(recipeId)).thenReturn(true);

        mockMvc.perform(get("/api/recipes/{id}/exists", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getRecipesByName() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(finder.findRecipeByName("Pancakes")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byName")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pancakes"));
    }

    @Test
    public void existsRecipeByName() throws Exception {
        when(finder.existsRecipeByName("Pancakes")).thenReturn(true);

        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getRecipesByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(finder.findRecipeByCategory(Category.DESSERT)).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DESSERT"));
    }

    @Test
    public void getRecipesByTag() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        when(finder.findRecipeByTag("Breakfast")).thenReturn(Collections.singletonList(sampleRecipe));

        mockMvc.perform(get("/api/recipes/byTag")
                        .param("tag", "Breakfast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags", hasSize(2)))
                .andExpect(jsonPath("$[0].tags[0]").value("Breakfast"));
    }

    @Test
    public void searchRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();

        when(finder.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));

        mockMvc.perform(get("/api/recipes/{id}", recipeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Pancakes"))
                .andExpect(jsonPath("$.category").value("DESSERT"))
                .andExpect(jsonPath("$.instructions")
                        .value("Mix everything and bake."))
                .andExpect(jsonPath("$.numberOfServings").value(4))
                .andExpect(jsonPath("$.ingredients[0].amount").value(200.0))
                .andExpect(jsonPath("$.ingredients[1].amount").value(300.0))
                .andExpect(jsonPath("$.tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.tags[1]").value("Sweet"));
    }


    @Test
    public void searchRecipeByName() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Page<Recipe> page = new PageImpl<>(List.of(sampleRecipe));

        when(finder.findRecipeByName(eq("Pancakes"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/recipes/search").param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(recipeId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Pancakes"))
                .andExpect(jsonPath("$.content[0].category").value("DESSERT"))
                .andExpect(jsonPath("$.content[0].instructions")
                        .value("Mix everything and bake."))
                .andExpect(jsonPath("$.content[0].numberOfServings").value(4))
                .andExpect(jsonPath("$.content[0].ingredients[0].amount").value(200.0))
                .andExpect(jsonPath("$.content[0].ingredients[1].amount").value(300.0))
                .andExpect(jsonPath("$.content[0].tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.content[0].tags[1]").value("Sweet"));

    }


    @Test
    public void searchRecipeByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        PageImpl<Recipe> pageOfRecipes = new PageImpl<>(List.of(sampleRecipe));
        when(finder.findRecipeByCategory(eq(Category.DESSERT), any(Pageable.class)))
                .thenReturn(pageOfRecipes);

        // when + then
        mockMvc.perform(get("/api/recipes/search")
                        .param("category", "dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(recipeId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Pancakes"))
                .andExpect(jsonPath("$.content[0].category").value("DESSERT"))
                .andExpect(jsonPath("$.content[0].tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.content[0].tags[1]").value("Sweet"));
    }


    @Test
    public void searchRecipeByTag() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        PageImpl<Recipe> pageOfRecipes = new PageImpl<>(List.of(sampleRecipe));
        when(finder.findRecipeByTag(eq("Breakfast"), any(Pageable.class)))
                .thenReturn(pageOfRecipes);

        // when + then
        mockMvc.perform(get("/api/recipes/search")
                        .param("tag", "Breakfast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(recipeId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Pancakes"))
                .andExpect(jsonPath("$.content[0].tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.content[0].tags[1]").value("Sweet"));
    }


    @Test
    public void searchRecipeWithoutParametersBadRequest() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();

        mockMvc.perform(get("/api/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Provide one of: name, category or tag"));
    }
}