package com.ania.cookbook.web.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeScalingService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.ScaleIngredientsUseCase.AdjustRecipe;
import com.ania.cookbook.domain.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeScalingController.class)
class RecipeScalingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeScalingService recipeScalingService;

    @Test
    public void scaleRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();
        int newServings = 4;

        RecipeScalingRequest request = RecipeScalingRequest.builder()
                .recipeId(recipeId)
                .servings(newServings)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        Category category = Category.MAIN_COURSE;

        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Flour"));
        Ingredient ingredient = Ingredient.newIngredient(product, 200.0f, Unit.G);

        Recipe newRecipe = Recipe.newRecipe(
                recipeId,
                "Test Recipe (4 servings)",
                category,
                Collections.singletonList(ingredient),
                "Mix ingredients",
                newServings,
                Arrays.asList("tag1", "tag2")
        );

        when(recipeScalingService.adjustRecipeByServings(any(AdjustRecipe.class)))
                .thenReturn(newRecipe);

        mockMvc.perform(post("/api/recipes/scaling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipeId.toString()))
                .andExpect(jsonPath("$.recipeName").value("Test Recipe (4 servings)"))
                .andExpect(jsonPath("$.category").value(category.toString()))
                .andExpect(jsonPath("$.instructions").value("Mix ingredients"))
                .andExpect(jsonPath("$.servings").value(newServings))
                .andExpect(jsonPath("$.tags[0]").value("tag1"))
                .andExpect(jsonPath("$.tags[1]").value("tag2"));
    }
}