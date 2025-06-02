package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.ReadRecipeService;
import com.ania.cookbook.application.services.implementations.recipe.RecipeService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.web.recipe.RecipeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReadRecipe readRecipeRepository;

    @Test
    void createRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();
        RecipeRequest request = new RecipeRequest(
                "Chocolate Cake",
                Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Sugar")), 200, Unit.G)),
                "Mix everything and bake",
                4,
                List.of("sweet", "easy")
        );

        Recipe recipe = Recipe.newRecipe(recipeId, request.recipeName(), request.category(),
                request.ingredients(), request.instructions(), request.numberOfServings(), request.tags());

        Mockito.when(recipeService.createRecipe(Mockito.any())).thenReturn(recipe);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipeName").value("Chocolate Cake"))
                .andExpect(jsonPath("$.category").value("DESSERT"))
                .andExpect(jsonPath("$.numberOfServings").value(4));
    }

    @Test
    void updateRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();
        RecipeRequest request = new RecipeRequest(
                "Updated Cake",
                Category.DESSERT,
                List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Sugar")), 300, Unit.G)),
                "New instructions",
                6,
                List.of("sweet", "best")
        );

        Recipe updatedRecipe = Recipe.newRecipe(recipeId, request.recipeName(), request.category(),
                request.ingredients(), request.instructions(), request.numberOfServings(), request.tags());

        Mockito.when(recipeService.updateRecipe(Mockito.any(UUID.class), Mockito.any())).thenReturn(updatedRecipe);

        mockMvc.perform(put("/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value("Updated Cake"))
                .andExpect(jsonPath("$.numberOfServings").value(6));
    }

    @Test
    void deleteRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();

        Mockito.doNothing().when(recipeService).deleteRecipe(Mockito.any());

        mockMvc.perform(delete("/recipes/" + recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DeleteRecipeCase(recipeId, "Recipe Name"))))
                .andDo(print())
                .andExpect(status().isNoContent());
    }



    @Test
    void notFoundForNonExistingRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();

        Mockito.when(recipeService.updateRecipe(Mockito.any(UUID.class), Mockito.any()))
                .thenThrow(new RecipeNotFoundException("Recipe not found."));

        mockMvc.perform(put("/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateRecipeCase("Updated",Category.DESSERT, List.of(),"",2,List.of()))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void notFoundForDeletingNonExistingRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();

        Mockito.doThrow(new RecipeNotFoundException("Recipe with given Id not found"))
                .when(recipeService).deleteRecipe(Mockito.any(DeleteRecipeCase.class));

        mockMvc.perform(delete("/recipes/" + recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DeleteRecipeCase(recipeId, "Valid Recipe Name"))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Recipe with given Id not found")));
    }
}