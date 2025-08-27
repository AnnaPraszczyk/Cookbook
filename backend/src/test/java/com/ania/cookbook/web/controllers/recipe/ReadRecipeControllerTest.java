package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.FindRecipeUseCase;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.exeptions.GlobalExceptionHandler;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import com.ania.cookbook.web.mappers.CategoryResolver;
import com.ania.cookbook.web.mappers.RecipeResponseMapper;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

    @Mock
    private RecipeResponseMapper responseMapper;

    @Mock
    private CategoryResolver categoryResolver;

    @InjectMocks
    private ReadRecipeController controller;

    private final UUID recipeId = UUID.randomUUID();

    private final List<Ingredient> ingredients = List.of(
            Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("flour")), 200f, Unit.G),
            Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("milk")), 300f, Unit.G)
    );

    @BeforeEach
    void setup() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(pageableResolver)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
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
    private RecipeResponse mapToResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory(),
                recipe.getIngredients().stream()
                        .map(i -> new IngredientResponse(
                                i.getProduct().getProductId(),
                                i.getProduct().getProductName(),
                                i.getAmount(),
                                i.getUnit()))
                        .toList(),
                recipe.getInstructions(),
                recipe.getNumberOfServings(),
                recipe.getTags()
        );
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
        List<RecipeResponse> mappedResponses = IntStream.range(0, 12)
                .mapToObj(i -> new RecipeResponse(
                        latestRecipes.get(i).getRecipeId(),
                        "Recipe " + i,
                        Category.DESSERT,
                        List.of(new IngredientResponse(
                                latestRecipes.get(i).getIngredients().getFirst().getProduct().getProductId(),
                                new ProductName("flour"),
                                100f,
                                Unit.G)),
                        "Do something " + i,
                        4,
                        List.of("Sweet", "Quick")
                ))
                .toList();
        when(finder.getLatestRecipes(12)).thenReturn(latestRecipes);
        when(responseMapper.toResponseList(latestRecipes)).thenReturn(mappedResponses);

        mockMvc.perform(get("/api/recipes/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[0].recipeName").value("Recipe 0"))
                .andExpect(jsonPath("$[0].category").value("Dessert"))
                .andExpect(jsonPath("$[0].tags[0]").value("Sweet"))
                .andExpect(jsonPath("$[0].ingredients[0].productName").value("flour"));
    }

    @Test
    void getRecipeById() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        RecipeResponse mappedResponse = mapToResponse(sampleRecipe);
        when(finder.findRecipeById(recipeId)).thenReturn(Optional.of(sampleRecipe));
        when(responseMapper.toResponse(sampleRecipe)).thenReturn(mappedResponse);

        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipeId.toString()))
                .andExpect(jsonPath("$.recipeName").value("Pancakes"))
                .andExpect(jsonPath("$.category").value("Dessert"))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags[0]").value("Breakfast"))
                .andExpect(jsonPath("$.tags[1]").value("Sweet"));
    }

    @Test
    void getRecipeById_NotFound() throws Exception {
        when(finder.findRecipeById(recipeId))
                .thenReturn(Optional.empty());
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
        RecipeResponse mappedResponse = mapToResponse(sampleRecipe);
        when(finder.findRecipeByName("Pancakes")).thenReturn(Collections.singletonList(sampleRecipe));
        when(responseMapper.toResponseList(Collections.singletonList(sampleRecipe)))
                .thenReturn(Collections.singletonList(mappedResponse));

        mockMvc.perform(get("/api/recipes/byName")
                        .param("recipeName", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value("Pancakes"))
                .andExpect(jsonPath("$[0].category").value("Dessert"))
                .andExpect(jsonPath("$[0].tags[0]").value("Breakfast"));
    }

    @Test
    void getRecipesByName_shouldReturnEmptyList_whenNoMatch() throws Exception {
        when(finder.findRecipeByName("Nonexistent")).thenReturn(Collections.emptyList());
        when(responseMapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/recipes/byName")
                        .param("recipeName", "Nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void existsRecipeByName() throws Exception {
        when(finder.existsRecipeByName("Pancakes")).thenReturn(true);
        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("recipeName", "Pancakes"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getRecipesByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        RecipeResponse mappedResponse = mapToResponse(sampleRecipe);
        when(categoryResolver.resolve("Dessert")).thenReturn(Category.DESSERT);
        when(finder.findRecipeByCategory(Category.DESSERT)).thenReturn(Collections.singletonList(sampleRecipe));
        when(responseMapper.toResponseList(Collections.singletonList(sampleRecipe)))
                .thenReturn(Collections.singletonList(mappedResponse));

        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "Dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Dessert"))
                .andExpect(jsonPath("$[0].recipeName").value("Pancakes"));
    }


    @Test
    void searchRecipeByName(){
        Recipe sampleRecipe = createSampleRecipe();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> page = new PageImpl<>(List.of(sampleRecipe), pageable, 1);
        RecipeResponse mappedResponse = mapToResponse(sampleRecipe);
        Page<RecipeResponse> mappedPage = new PageImpl<>(List.of(mappedResponse), pageable, 1);
        when(finder.findRecipeByName(eq("Pancakes"), any(Pageable.class))).thenReturn(page);
        when(responseMapper.toResponsePage(page)).thenReturn(mappedPage);
        ResponseEntity<Page<RecipeResponse>> response = controller.search("Pancakes", null, pageable);
        Page<RecipeResponse> result = response.getBody();

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Pancakes", result.getContent().getFirst().recipeName());
    }

    @Test
    void searchRecipeByCategory() throws Exception {
        Recipe sampleRecipe = createSampleRecipe();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Recipe> pageOfRecipes = new PageImpl<>(List.of(sampleRecipe), pageable, 1);
        RecipeResponse mappedResponse = mapToResponse(sampleRecipe);
        Page<RecipeResponse> mappedPage = new PageImpl<>(List.of(mappedResponse), pageable, 1);
        when(categoryResolver.resolve("Dessert")).thenReturn(Category.DESSERT);
        when(finder.findRecipeByCategory(eq(Category.DESSERT), any(Pageable.class))).thenReturn(pageOfRecipes);
        when(responseMapper.toResponsePage(pageOfRecipes)).thenReturn(mappedPage);

        mockMvc.perform(get("/api/recipes/search")
                        .param("category", "Dessert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeId").value(sampleRecipe.getRecipeId().toString()))
                .andExpect(jsonPath("$.content[0].recipeName").value("Pancakes"))
                .andExpect(jsonPath("$.content[0].category").value("Dessert"))
                .andExpect(jsonPath("$.content[0].instructions").value("Mix everything and bake."))
                .andExpect(jsonPath("$.content[0].numberOfServings").value(4))
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
                .andExpect(content().string("\"Provide at least name or category\""));
    }

    @Test
    void returnRecipesByNameAndCategory() throws Exception {
        Recipe recipe = createSampleRecipe();
        RecipeResponse response = mapToResponse(recipe);
        Pageable pageable = PageRequest.of(0, 20, Sort.by("recipeName"));
        when(categoryResolver.resolve("Dessert")).thenReturn(Category.DESSERT);
        when(finder.findRecipeByNameAndCategory(eq("Pancakes"), eq(Category.DESSERT), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recipe), pageable, 1));
        when(responseMapper.toResponsePage(any())).thenReturn(new PageImpl<>(List.of(response), pageable,1));


        mockMvc.perform(get("/api/recipes/search")
                        .param("recipeName", "Pancakes")
                        .param("category", "Dessert")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("Pancakes"))
                .andExpect(jsonPath("$.content[0].category").value("Dessert"));
        verify(finder).findRecipeByNameAndCategory(eq("Pancakes"), eq(Category.DESSERT), any(Pageable.class));
    }

    @Test
    void returnRecipesByNameOnly() throws Exception {
        Recipe recipe = createSampleRecipe();
        RecipeResponse response = mapToResponse(recipe);
        Pageable pageable = PageRequest.of(0, 20, Sort.by("recipeName"));
        when(finder.findRecipeByName("Pancakes", pageable))
                .thenReturn(new PageImpl<>(List.of(recipe), pageable, 1));
        when(responseMapper.toResponsePage(any()))
                .thenReturn(new PageImpl<>(List.of(response), pageable, 1));

        mockMvc.perform(get("/api/recipes/search")
                        .param("recipeName", "Pancakes")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("Pancakes"));
        verify(finder).findRecipeByName("Pancakes", pageable);
    }

    @Test
    void returnRecipesByCategoryOnly() throws Exception {
        Recipe recipe = createSampleRecipe();
        RecipeResponse response = mapToResponse(recipe);
        Pageable pageable = PageRequest.of(0, 20, Sort.by("recipeName"));
        when(categoryResolver.resolve("DESSERT")).thenReturn(Category.DESSERT);
        when(finder.findRecipeByCategory(Category.DESSERT, pageable))
                .thenReturn(new PageImpl<>(List.of(recipe), pageable, 1));
        when(responseMapper.toResponsePage(any()))
                .thenReturn(new PageImpl<>(List.of(response), pageable, 1));

        mockMvc.perform(get("/api/recipes/search")
                        .param("category", "DESSERT")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].category").value("Dessert"));
        verify(finder).findRecipeByCategory(Category.DESSERT, pageable);
    }

    @Test
    void returnBadRequestWhenNoParamsProvided() throws Exception {
        mockMvc.perform(get("/api/recipes/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Provide at least name or category\""));
    }
}