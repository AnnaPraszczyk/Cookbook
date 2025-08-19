package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
public class RecipeCommand {
    private final String recipeName;
    private final Category category;
    private final List<IngredientRequest> ingredients;
    private final String instructions;
    private final int numberOfServings;
    private final List<String> tags;

    @Builder
    public RecipeCommand(String recipeName, Category category, List<IngredientRequest> ingredients,
                         String instructions, int numberOfServings, List<String> tags) {
        if (isBlank(recipeName)) throw new IllegalArgumentException("Recipe name cannot be blank");
        this.recipeName = recipeName;
        if (category == null) throw new IllegalArgumentException("Recipe category cannot be null");
        this.category = category;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        if (isBlank(instructions)) throw new IllegalArgumentException("Recipe instructions cannot be blank");
        this.instructions = instructions;
        if (numberOfServings < 0) throw new IllegalArgumentException("Recipe number of servings cannot be negative");
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
