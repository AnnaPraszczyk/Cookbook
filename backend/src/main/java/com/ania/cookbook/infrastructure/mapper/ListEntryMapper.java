package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ListEntryMapper {
    private final RecipeMapper recipeMapper;
    private final SavedListMapperWithoutEntries savedListMapper;

    public ListEntry toDomain(ListEntryEntity entity) {
        Recipe recipe = recipeMapper.toDomain(entity.getRecipe());
        SavedList savedList = savedListMapper.toDomainWithoutEntries(entity.getSavedList());

        return ListEntry.builder()
                .entryId(entity.getEntryId())
                .recipe(recipe)
                .savedRecipeList(savedList)
                .portions(entity.getPortions())
                .build();
    }

    public ListEntryEntity toEntity(ListEntry domain, SavedListEntity savedListEntity) {
        return ListEntryEntity.builder()
                .entryId(domain.getEntryId())
                .recipe(recipeMapper.toEntity(domain.getRecipe()))
                .savedList(savedListEntity)
                .portions(domain.getPortions())
                .build();
    }
}
