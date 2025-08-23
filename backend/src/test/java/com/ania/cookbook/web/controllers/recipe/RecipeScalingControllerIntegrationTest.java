package com.ania.cookbook.web.controllers.recipe;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.ania.cookbook.web.recipe.RecipeResponse;
import com.ania.cookbook.web.recipe.RecipeScalingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.greaterThan;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RecipeScalingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID createdRecipeId;

    @BeforeEach
    void setup() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Scalable Recipe",
                Category.MAIN_COURSE,
                List.of(new IngredientRequest("Flour", 500, Unit.G)),
                "Mix and bake",
                4,
                List.of("Baking")
        );
        String json = objectMapper.writeValueAsString(request);
        String response = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        RecipeResponse created = objectMapper.readValue(response, RecipeResponse.class);
        createdRecipeId = created.recipeId();
    }

    @Test
    void scaleRecipeSuccessfully() throws Exception {
        RecipeScalingRequest scalingRequest = new RecipeScalingRequest(createdRecipeId, 8);
        String json = objectMapper.writeValueAsString(scalingRequest);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfServings").value(8))
                .andExpect(jsonPath("$.ingredients[0].amount").value(1000.0));
    }

    @Test
    void returnBadRequest_whenServingsIsNegative() throws Exception {
        RecipeScalingRequest scalingRequest = new RecipeScalingRequest(createdRecipeId, -2);
        String json = objectMapper.writeValueAsString(scalingRequest);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequest_whenRecipeIdIsNull() throws Exception {
        RecipeScalingRequest scalingRequest = new RecipeScalingRequest(null, 2);
        String json = objectMapper.writeValueAsString(scalingRequest);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnNotFound_whenRecipeDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        RecipeScalingRequest scalingRequest = new RecipeScalingRequest(fakeId, 2);
        String json = objectMapper.writeValueAsString(scalingRequest);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void autoCalculateServings_whenServingsIsZero() throws Exception {
        RecipeScalingRequest scalingRequest = new RecipeScalingRequest(createdRecipeId, 0);
        String json = objectMapper.writeValueAsString(scalingRequest);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfServings").value(greaterThan(0)));
    }
}
