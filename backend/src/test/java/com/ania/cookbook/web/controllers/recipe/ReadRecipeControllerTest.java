package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.exeptions.GlobalExceptionHandler;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


@ExtendWith(MockitoExtension.class)
class ReadRecipeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FindRecipeUseCase finder;

    @InjectMocks
    private ReadRecipeController controller;

    @BeforeEach
    void setup() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(pageableResolver)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

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
    void getLatestRecipes() throws Exception {
        List<Recipe> latestRecipes = IntStream.range(0, 12)
                .mapToObj(i -> Recipe.builder()
                        .recipeId(UUID.randomUUID())
                        .recipeName("Recipe " + i)
                        .category(Category.DESSERT)
                        .ingredients(List.of(
                                Ingredient.newIngredient(
                                        Product.newProduct(UUID.randomUUID(), new ProductName("flour")), 100f, Unit.G)))
                        .instructions("Do something " + i)
                        .numberOfServings(4)
                        .tags(List.of("Sweet", "Quick"))
                        .build())
                .toList();

        when(finder.getLatestRecipes(12)).thenReturn(latestRecipes);
        mockMvc.perform(get("/api/recipes/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[0].name").value("Recipe 0"))
                .andExpect(jsonPath("$[0].category").value("Dessert"))
                .andExpect(jsonPath("$[0].tags[0]").value("Sweet"))
                .andExpect(jsonPath("$[0].ingredients[0].productName").value("flour"));
    }

    @Test
    void getRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();

        when(finder.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));
        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Pancakes"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.tags[1]").value("Sweet"));
    }

    @Test
    void getRecipeById_NotFound() throws Exception {
        when(finder.findRecipeById(recipeId))
                .thenThrow(new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Unable to find the recipe because it does not exist.\""));
    }

    @Test
    void existsRecipeById() throws Exception {
        when(finder.existsRecipeById(recipeId)).thenReturn(true);
        mockMvc.perform(get("/api/recipes/{id}/exists", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getRecipesByName() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();

        when(finder.findRecipeByName("Pancakes")).thenReturn(Collections.singletonList(sampleRecipe));
        mockMvc.perform(get("/api/recipes/byName")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pancakes"));
    }

    @Test
    void existsRecipeByName() throws Exception {
        when(finder.existsRecipeByName("Pancakes")).thenReturn(true);
        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("name", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getRecipesByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();

        when(finder.findRecipeByCategory(Category.DESSERT)).thenReturn(Collections.singletonList(sampleRecipe));
        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Dessert"));
    }

    @Test
    void searchRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();

        when(finder.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));
        mockMvc.perform(get("/api/recipes/{id}", recipeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Pancakes"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.instructions")
                        .value("Mix everything and bake."))
                .andExpect(jsonPath("$.numberOfServings").value(4))
                .andExpect(jsonPath("$.ingredients[0].amount").value(200.0))
                .andExpect(jsonPath("$.ingredients[1].amount").value(300.0))
                .andExpect(jsonPath("$.tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.tags[1]").value("Sweet"));
    }

    @Test
    void searchRecipeByName(){
        Recipe sampleRecipe = createSampleRecipe();
        Page<Recipe> page = new PageImpl<>(List.of(sampleRecipe));

        when(finder.findRecipeByName(eq("Pancakes"), any(Pageable.class)))
                .thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReadRecipeResponse> result = controller.search("Pancakes", null, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals("Pancakes", result.getContent().getFirst().getName());
    }

    @Test
    void searchRecipeByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Recipe> pageOfRecipes = new PageImpl<>(List.of(sampleRecipe), pageable, 1);

        when(finder.findRecipeByCategory(eq(Category.DESSERT), any(Pageable.class)))
                .thenReturn(pageOfRecipes);
        ReadRecipeResponse expectedResponse = ReadRecipeResponse.from(sampleRecipe);
        mockMvc.perform(get("/api/recipes/search")
                        .param("category", "Dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(expectedResponse.getId().toString()))
                .andExpect(jsonPath("$.content[0].name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.content[0].category").value("Dessert"))
                .andExpect(jsonPath("$.content[0].instructions").value(expectedResponse.getInstructions()))
                .andExpect(jsonPath("$.content[0].numberOfServings").value(expectedResponse.getNumberOfServings()))
                .andExpect(jsonPath("$.content[0].ingredients[0].productName").value("flour"))
                .andExpect(jsonPath("$.content[0].ingredients[0].amount").value(200.0))
                .andExpect(jsonPath("$.content[0].ingredients[1].productName").value("milk"))
                .andExpect(jsonPath("$.content[0].ingredients[1].amount").value(300.0))
                .andExpect(jsonPath("$.content[0].tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.content[0].tags[1]").value("Sweet"));

    }

    @Test
    void searchRecipeWithoutParametersBadRequest() throws Exception {
        mockMvc.perform(get("/api/recipes/search"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Provide either name or category\""));

    }
}