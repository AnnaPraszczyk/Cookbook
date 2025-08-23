package com.ania.cookbook.web.list;
import com.ania.cookbook.web.recipe.RecipeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListEntryResponse {
    private UUID entryId;
    private int portions;
    private RecipeResponse recipe;
}
