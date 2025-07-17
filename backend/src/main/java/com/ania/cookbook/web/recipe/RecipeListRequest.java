package com.ania.cookbook.web.recipe;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListRequest {
    private UUID recipeId;
    private String listName;
    private Boolean confirm;
    private Integer portions;
}
