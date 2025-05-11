package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.converters.IngredientsJsonConverter;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RecipeConverter  implements Converter<RecipeEntity, Recipe> {

    @Override
    public Recipe convert(RecipeEntity entity) {
        if (entity == null) {
            throw new RecipeValidationException("RecipeEntity cannot be null");
        }

        return Recipe.newRecipe(
                entity.getRecipeId(),
                entity.getRecipeName(),
                entity.getCategory(),
                IngredientsJsonConverter.listFromJson(entity.getIngredientsJson()),
                entity.getInstructions(),
                entity.getNumberOfServings(),
                entity.getTags() != null ? List.copyOf(entity.getTags()) : List.of());
    }

}
