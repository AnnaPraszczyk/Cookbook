package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.model.Recipe;
import java.util.List;
import java.util.Map;

public interface ShoppingListUseCase {

    Map<String, Float> generateShoppingList(List<Recipe> recipes);
}
