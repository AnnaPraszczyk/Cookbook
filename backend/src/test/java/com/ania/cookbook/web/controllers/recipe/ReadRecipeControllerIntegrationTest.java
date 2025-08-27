package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class ReadRecipeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID recipeId;

    @BeforeEach
    void setup() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Test Recipe",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Salt", 100, Unit.G)),
                "Boil water",
                2,
                List.of("Quick")
        );
        String json = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RecipeResponse created = objectMapper.readValue(response, RecipeResponse.class);
        recipeId = created.recipeId();
    }

    @Test
    void returnLatestRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value("Test Recipe"));
    }

    @Test
    void returnRecipeById() throws Exception {
        mockMvc.perform(get("/api/recipes/" + recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value("Test Recipe"));
    }

    @Test
    void returnNotFound_whenRecipeIdDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/recipes/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnTrue_whenRecipeExistsById() throws Exception {
        mockMvc.perform(get("/api/recipes/" + recipeId + "/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void returnFalse_whenRecipeDoesNotExistById() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/recipes/" + fakeId + "/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void returnRecipesByName() throws Exception {
        mockMvc.perform(get("/api/recipes/byName")
                        .param("recipeName", "Test Recipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value("Test Recipe"));
    }

    @Test
    void returnTrue_whenRecipeExistsByName() throws Exception {
        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("recipeName", "Test Recipe"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void returnFalse_whenRecipeDoesNotExistByName() throws Exception {
        mockMvc.perform(get("/api/recipes/byName/exists")
                        .param("recipeName", "Ghost"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void returnRecipesByCategory() throws Exception {
        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "Main Course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Main Course"));
    }

    @Test
    void returnBadRequest_whenCategoryIsInvalid() throws Exception {
        mockMvc.perform(get("/api/recipes/byCategory")
                        .param("category", "InvalidCategory"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid category")));

    }

    @Test
    void returnRecipeByName() throws Exception {

        mockMvc.perform(get("/api/recipes/search")
                        .param("recipeName", "Test Recipe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("Test Recipe"))
                .andExpect(jsonPath("$.content[0].category").value("Main Course"))
                .andExpect(jsonPath("$.content[0].instructions").value("Boil water"));
    }

    @Test
    void returnRecipeByCategory() throws Exception {

        mockMvc.perform(get("/api/recipes/search")
                        .param("category", "Main Course")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("Test Recipe"))
                .andExpect(jsonPath("$.content[0].category").value("Main Course"));
    }

    @Test
    void returnRecipeByNameAndCategory() throws Exception {

        mockMvc.perform(get("/api/recipes/search")
                        .param("recipeName", "Test")
                        .param("category", "Main Course")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("Test Recipe"))
                .andExpect(jsonPath("$.content[0].category").value("Main Course"));
    }

    @Test
    void returnBadRequestWhenNoParamsProvided() throws Exception {
        mockMvc.perform(get("/api/recipes/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provide at least name or category"));
    }

    @Test
    void respectPagination() throws Exception {
        mockMvc.perform(get("/api/recipes/search")
                        .param("recipeName", "Test")
                        .param("page", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.number").value(0));
    }
}
