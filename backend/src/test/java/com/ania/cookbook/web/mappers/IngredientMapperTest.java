package com.ania.cookbook.web.mappers;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Unit;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IngredientMapperTest {
    private IngredientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new IngredientMapper();
    }
    @Test
    void shouldMapIngredientToResponseCorrectly() {
        // given
        UUID productId = UUID.randomUUID();
        ProductName productName = ProductName.from("Sugar");
        Product product = Product.newProduct(productId, productName);
        Ingredient ingredient = Ingredient.newIngredient(product, 200, Unit.G);
        IngredientResponse response = mapper.toResponse(ingredient);

        assertEquals(productId, response.productId());
        assertEquals(productName, response.productName());
        assertEquals(200, response.amount());
        assertEquals(Unit.G, response.unit());
    }

}