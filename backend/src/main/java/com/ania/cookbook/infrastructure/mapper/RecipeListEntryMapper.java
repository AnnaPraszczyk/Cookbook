package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.ania.cookbook.web.recipe.RecipeListEntryResponse;
import org.springframework.stereotype.Component;

@Component
public class RecipeListEntryMapper {
    private final RecipeMapper recipeMapper;

    public RecipeListEntryMapper(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public RecipeListEntryResponse toResponse(RecipeListEntry entry) {
        return RecipeListEntryResponse.builder()
                .entryId(entry.getEntryId())
                .portions(entry.getPortions())
                .recipe(ReadRecipeResponse.from(recipeMapper.toDomain(entry.getRecipe())))
                .build();
    }
}
