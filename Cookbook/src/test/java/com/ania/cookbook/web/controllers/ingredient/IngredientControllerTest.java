package com.ania.cookbook.web.controllers.ingredient;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateIngredientSuccessfully() throws Exception {

        ProductName productName = new ProductName("Sugar");
        Product product = Product.newProduct(UUID.randomUUID(), productName);
        Ingredient ingredient = Ingredient.newIngredient(product, 500, Unit.G);
        IngredientRequest request = new IngredientRequest(productName, 500, Unit.G);

        Mockito.when(ingredientService.createIngredient(Mockito.any(ProductName.class), Mockito.anyFloat(), Mockito.any(Unit.class)))
                .thenReturn(ingredient);

        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName.name").value("Sugar"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.unit").value("G"));
    }
}