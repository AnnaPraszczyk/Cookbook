package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.recipe.ReadRecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ListEntryResponseMapper {
    private final RecipeMapper recipeMapper;

    public ListEntryResponse toResponse(ListEntry entry) {
        if (entry == null) {
            throw new ListValidationException("ListEntryEntity cannot be null");
        }
        Recipe recipe = entry.getRecipe();
        ReadRecipeResponse recipeResponse = recipe != null ? ReadRecipeResponse.from(recipe) : null;

        return ListEntryResponse.builder()
                .entryId(entry.getEntryId())
                .portions(entry.getPortions())
                .recipe(recipeResponse)
                .build();
    }

    public List<ListEntryResponse> toResponseList(List<ListEntry> entries) {
        return entries.stream()
                .map(this::toResponse)
                .toList();
    }
}
