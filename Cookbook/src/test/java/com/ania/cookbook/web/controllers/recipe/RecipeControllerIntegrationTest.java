package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.model.Category;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class RecipeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createRecipe() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Pasta",
                Category.MAIN_COURSE,
                Collections.emptyList(),
                "Boil pasta and add sauce",
                2,
                Collections.singletonList("Italian")
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipeName").value("Pasta"))
                .andExpect(jsonPath("$.category").value("MAIN_COURSE"))
                .andExpect(jsonPath("$.instructions").value("Boil pasta and add sauce"))
                .andExpect(jsonPath("$.numberOfServings").value(2))
                .andExpect(jsonPath("$.tags[0]").value("Italian"));
    }

    @Test
    public void updateRecipe() throws Exception {
        RecipeRequest createRequest = new RecipeRequest(
                "Salad",
                Category.APPETIZER,
                Collections.emptyList(),
                "Mix veggies",
                1,
                Collections.singletonList("Healthy")
        );
        String createJson = objectMapper.writeValueAsString(createRequest);

        String responseContent = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RecipeResponse createdRecipe = objectMapper.readValue(responseContent, RecipeResponse.class);
        UUID recipeId = createdRecipe.recipeId();

        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Updated Salad",
                Category.APPETIZER,
                Collections.emptyList(),
                "Mix veggies with dressing",
                2,
                Collections.singletonList("Fresh")
        );
        String updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value("Updated Salad"))
                .andExpect(jsonPath("$.instructions").value("Mix veggies with dressing"))
                .andExpect(jsonPath("$.numberOfServings").value(2))
                .andExpect(jsonPath("$.tags[0]").value("Fresh"));
    }

    @Test
    public void deleteRecipe() throws Exception {
        RecipeRequest createRequest = new RecipeRequest(
                "Soup",
                Category.MAIN_COURSE,
                Collections.emptyList(),
                "Boil water and add vegetables",
                3,
                Collections.singletonList("Winter")
        );
        String createJson = objectMapper.writeValueAsString(createRequest);

        String responseContent = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RecipeResponse createdRecipe = objectMapper.readValue(responseContent, RecipeResponse.class);
        UUID recipeId = createdRecipe.recipeId();
        String recipeName = createdRecipe.recipeName();

        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(recipeId,recipeName);
        String deleteJson = objectMapper.writeValueAsString(deleteRequest);

        mockMvc.perform(delete("/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isNoContent());
    }
}
