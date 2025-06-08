package com.ania.cookbook.web.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
public class RecipeListRequest {
    private UUID recipeId;
    private String listName;
    private Boolean confirm;
}
