package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.ShoppingListUseCase;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ShoppingListService implements ShoppingListUseCase {
    @Override
    public Map<String, Float> generateShoppingList(List<Recipe> recipes) {
        Map<String, Float> shoppingList = new HashMap<>();
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                String productName = ingredient.getProduct().getProductName();
                float amountInGrams = ingredient.getUnit().toGrams(ingredient.getAmount());

                shoppingList.merge(productName, amountInGrams, Float::sum);
            }
        }
        return shoppingList;
    }
}
