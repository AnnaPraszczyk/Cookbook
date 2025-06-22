package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {


    public Recipe toDomain(RecipeEntity e) {
        return Recipe.newRecipe(
                e.getRecipeId(),
                e.getRecipeName(),
                e.getCategory(),
                e.getIngredients(),
                e.getInstructions(),
                e.getNumberOfServings(),
                e.getTags()
        );
    }

    public RecipeEntity toEntity(Recipe d) {
        return RecipeEntity.newRecipeEntity(
                d.getRecipeId(),
                d.getRecipeName(),
                d.getCategory(),
                d.getIngredients(),
                d.getInstructions(),
                d.getNumberOfServings(),
                d.getTags()
        );
    }


}
