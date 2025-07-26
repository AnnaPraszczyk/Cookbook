package com.ania.cookbook.web.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecipeListResponse {
    private ListName listName;
    private String listDescription;
    private Integer expectedPortions;
    private List<RecipeListEntryResponse> recipes;
}
