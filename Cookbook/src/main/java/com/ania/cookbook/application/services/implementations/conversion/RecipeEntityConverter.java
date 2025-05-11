package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.converters.IngredientsJsonConverter;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RecipeEntityConverter implements Converter<Recipe, RecipeEntity> {

    @Override
    public RecipeEntity convert(Recipe recipe) {
        if (recipe == null) {
            throw new RecipeValidationException("RecipeEntity cannot be null");
        }

        return RecipeEntity.newRecipeEntity(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getCategory(),
                IngredientsJsonConverter.listToJson(recipe.getIngredients()),
                recipe.getInstructions(),
                recipe.getNumberOfServings(),
                recipe.getTags() != null ? List.copyOf(recipe.getTags()) : List.of()

        );
    }


}
