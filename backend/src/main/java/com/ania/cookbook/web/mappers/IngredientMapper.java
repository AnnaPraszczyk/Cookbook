package com.ania.cookbook.web.mappers;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientMapper {
    public IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getProduct().getProductId(),
                ingredient.getProduct().getProductName(),
                ingredient.getAmount(),
                ingredient.getUnit()
        );
    }
}
