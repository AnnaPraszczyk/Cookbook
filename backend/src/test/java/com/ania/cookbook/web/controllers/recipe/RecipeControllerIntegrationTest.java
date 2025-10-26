package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRecipe() throws Exception {
        List<IngredientRequest> ingredients = List.of(
                new IngredientRequest("Sugar", 200, Unit.G)
        );
        RecipeRequest request = new RecipeRequest("Pasta", Category.MAIN_COURSE, ingredients, "Boil pasta and add sauce",
                2, Collections.singletonList("Italian"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipeName").value("Pasta"))
                .andExpect(jsonPath("$.category").value("Main Course"))
                .andExpect(jsonPath("$.instructions").value("Boil pasta and add sauce"))
                .andExpect(jsonPath("$.numberOfServings").value(2))
                .andExpect(jsonPath("$.tags[0]").value("Italian"));
    }

    @Test
    void createRecipe_shouldReturnBadRequest_whenIngredientAmountIsZero() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Invalid",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Sugar", 0, Unit.G)),
                "Cook",
                2,
                List.of("Tag")
        );
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRecipe() throws Exception {
        List<IngredientRequest> ingredients = List.of(
                new IngredientRequest("Sugar", 200, Unit.G)
        );
        RecipeRequest createRequest = new RecipeRequest("Salad", Category.APPETIZER, ingredients, "Mix veggies",
                1, Collections.singletonList("Healthy"));
        String createJson = objectMapper.writeValueAsString(createRequest);
        String responseContent = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        RecipeResponse createdRecipe = objectMapper.readValue(responseContent, RecipeResponse.class);
        UUID recipeId = createdRecipe.recipeId();
        List<IngredientRequest> updatedIngredients = List.of(
                new IngredientRequest("Sugar", 100, Unit.G)
        );
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Updated Salad",
                Category.APPETIZER,
                updatedIngredients,
                "Mix veggies with dressing",
                2,
                List.of("Fresh")
        );
        String updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value("Updated Salad"))
                .andExpect(jsonPath("$.instructions").value("Mix veggies with dressing"))
                .andExpect(jsonPath("$.numberOfServings").value(2))
                .andExpect(jsonPath("$.tags[0]").value("Fresh"));
    }

    @Test
    void updateRecipe_shouldReturnNotFound_whenRecipeDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Ghost Recipe",
                Category.DESSERT,
                List.of(new IngredientRequest("Sugar", 100, Unit.G)),
                "No instructions",
                1,
                List.of("Ghost")
        );
        String json = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/recipes/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecipe() throws Exception {
        List<IngredientRequest> ingredients = List.of(new IngredientRequest("Sugar", 200, Unit.G));
        RecipeRequest createRequest = new RecipeRequest(
                "Soup",
                Category.MAIN_COURSE,
                ingredients,
                "Boil water and add vegetables",
                3,
                Collections.singletonList("Winter")
        );
        String createJson = objectMapper.writeValueAsString(createRequest);

        String responseContent = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        RecipeResponse createdRecipe = objectMapper.readValue(responseContent, RecipeResponse.class);
        UUID recipeId = createdRecipe.recipeId();
        String recipeName = createdRecipe.recipeName();
        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(recipeId,recipeName);
        String deleteJson = objectMapper.writeValueAsString(deleteRequest);
        mockMvc.perform(delete("/api/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRecipe_shouldReturnNotFound_whenRecipeDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(fakeId, "Ghost");
        String json = objectMapper.writeValueAsString(deleteRequest);

        mockMvc.perform(delete("/api/recipes/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}
