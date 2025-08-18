package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ListEntryMapper {
    private final RecipeMapper recipeMapper;
    private final SavedListMapper savedListMapper;

    public ListEntry toDomain(ListEntryEntity entity) {
        Recipe recipe = recipeMapper.toDomain(entity.getRecipe());
        SavedList savedList = savedListMapper.toDomain(entity.getSavedList());

        return ListEntry.builder()
                .entryId(entity.getEntryId())
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(entity.getPortions())
                .build();
    }

    public ListEntryEntity toEntity(ListEntry domain) {
        return ListEntryEntity.builder()
                .entryId(domain.getEntryId())
                .recipe(recipeMapper.toEntity(domain.getRecipe()))
                .portions(domain.getPortions())
                .build();
    }
}
