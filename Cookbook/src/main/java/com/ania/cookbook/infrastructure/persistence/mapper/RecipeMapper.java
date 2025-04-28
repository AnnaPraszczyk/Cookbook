package com.ania.cookbook.infrastructure.persistence.mapper;

import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;

public class RecipeMapper {

        public static Recipe toDomain(RecipeEntity entity) {
            if (entity == null) {throw new IllegalArgumentException("RecipeEntity cannot be null");}
            return Recipe.newRecipe(
                    entity.getRecipeId(),
                    entity.getRecipeName(),
                    entity.getCategories(),
                    entity.getIngredients(),
                    entity.getInstructions(),
                    entity.getNumberOfServings()
            );
        }

        public static RecipeEntity toEntity(Recipe domain) {
            if (domain == null) {throw new IllegalArgumentException("Recipe cannot be null");}
            return RecipeEntity.newRecipeEntity(
                    domain.getRecipeId(),
                    domain.getRecipeName(),
                    domain.getCategories(),
                    domain.getIngredients(),
                    domain.getInstructions(),
                    domain.getNumberOfServings()
            );
        }
}

