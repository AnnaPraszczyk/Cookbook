package com.ania.cookbook.web.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListEntryResponse {
    private UUID entryId;
    private int portions;
    private ReadRecipeResponse recipe;
}
