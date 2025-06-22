package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {


    private Ingredient fromJson(IngredientJson j) {
        Product p = Product.newProduct(j.getProductId(), new ProductName(j.getProductName()));
        return Ingredient.newIngredient(p, j.getAmount(), j.getUnit());
    }


    public Recipe toDomain(RecipeEntity e) {
        return Recipe.newRecipe(
                e.getRecipeId(),
                e.getRecipeName(),
                e.getCategory(),
                e.getIngredients().stream().map(this::fromJson).toList(),
                e.getInstructions(),
                e.getNumberOfServings(),
                e.getTags()
        );
    }

    private IngredientJson toJson(Ingredient ing) {
        return new IngredientJson(ing.getProduct().getProductId(),
                ing.getProduct().getProductName().name(),
                ing.getAmount(),
                ing.getUnit());
    }
    public RecipeEntity toEntity(Recipe d) {
        return RecipeEntity.newRecipeEntity(
                d.getRecipeId(),
                d.getRecipeName(),
                d.getCategory(),
                d.getIngredients().stream().map(this::toJson).toList(),
                d.getInstructions(),
                d.getNumberOfServings(),
                d.getTags()
        );
    }


}
