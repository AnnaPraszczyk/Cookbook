package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.domain.model.RecipeListEntryDomain;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import com.ania.cookbook.web.recipe.RecipeListEntryResponse;
import org.springframework.stereotype.Component;
import java.util.List;

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

    public RecipeListEntryDomain toDomain(RecipeListEntry entry) {
        return RecipeListEntryDomain.builder()
                .entryId(entry.getEntryId())
                .recipe(recipeMapper.toDomain(entry.getRecipe()))
                .portions(entry.getPortions())
                .build();
    }

    public RecipeListEntry toEntity(RecipeListEntryDomain domain) {
        return RecipeListEntry.builder()
                .entryId(domain.getEntryId())
                .recipe(recipeMapper.toEntity(domain.getRecipe()))
                .portions(domain.getPortions())
                .build();
    }
}
