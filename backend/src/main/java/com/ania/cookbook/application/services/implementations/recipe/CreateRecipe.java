package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
public class CreateRecipe {
    private final String recipeName;
    private final Category category;
    private final List<IngredientRequest> ingredients;
    private final String instructions;
    private final int numberOfServings;
    private final List<String> tags;

    @Builder
    public CreateRecipe(String recipeName, Category category, List<IngredientRequest> ingredients,
                        String instructions, int numberOfServings, List<String> tags) {
        if (isBlank(recipeName)) throw new RecipeValidationException("Recipe name cannot be null or empty.");
        this.recipeName = recipeName;
        if (category == null) throw new RecipeValidationException("Recipe category cannot be null.");
        this.category = category;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        if (isBlank(instructions)) throw new RecipeValidationException("Recipe instructions cannot be null or empty.");
        this.instructions = instructions;
        if (numberOfServings < 0) throw new RecipeValidationException("Recipe number of servings cannot be negative.");
        this.numberOfServings = numberOfServings;
        this.tags = tags;
    }

    public Recipe toDomain(ProductUseCase productUseCase) {
        List<Ingredient> domainIngredients = ingredients.stream()
                .map(i -> {
                    ProductName name = ProductName.from(i.productName());
                    Product product = productUseCase.findProductByName(name)
                            .orElseGet(() -> productUseCase.addProduct(name));
                    return Ingredient.newIngredient(product, i.amount(), i.unit());
                })
                .toList();
        return Recipe.newRecipe(
                UUID.randomUUID(),
                recipeName,
                category,
                domainIngredients,
                instructions,
                numberOfServings,
                tags
        );
    }
}
