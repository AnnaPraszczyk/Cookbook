package com.ania.cookbook.web.recipe;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadRecipeRequest {
    private UUID recipeId;
    private String recipeName;
    private String category;
    private String tag;
    private Integer page = 0;
    private Integer size = 10;
}
