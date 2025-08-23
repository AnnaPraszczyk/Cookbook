package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import com.ania.cookbook.infrastructure.persistence.list.ListEntryRepository;
import com.ania.cookbook.infrastructure.persistence.list.SavedListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ListEntryMapper {
    private final RecipeMapper recipeMapper;
    private final SavedListMapperWithoutEntries savedListMapper;
    private final SavedListRepository savedListRepository;
    private final ListEntryRepository repository;

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

    public ListEntryEntity toEntityWithLookup(ListEntry domain) {
        String listName = domain.getSavedRecipeList().getListName().name();
        SavedListEntity savedListEntity = savedListRepository.findByListName(listName)
                .orElseThrow(() -> new ListValidationException("Saved list entity not found for name: " + listName));
        if (domain.getEntryId() != null && repository.existsById(domain.getEntryId())) {
            ListEntryEntity existing = repository.findById(domain.getEntryId())
                    .orElseThrow(() -> new ListValidationException("Entry not found: " + domain.getEntryId()));
            existing.setPortions(domain.getPortions());
            existing.setRecipe(recipeMapper.toEntity(domain.getRecipe()));
            existing.setSavedList(savedListEntity);
            return existing;
        }
        return ListEntryEntity.builder()
                .recipe(recipeMapper.toEntity(domain.getRecipe()))
                .savedList(savedListEntity)
                .portions(domain.getPortions())
                .build();
    }
}
