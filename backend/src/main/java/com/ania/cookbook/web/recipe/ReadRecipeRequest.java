package com.ania.cookbook.web.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadRecipeRequest {
    @NotNull private UUID recipeId;
    @NotBlank private String recipeName;
    @NotNull private String category;
    @Size(max = 100) private String tag;
    @Min(0) private Integer page = 0;
    @Min(1) private Integer size = 10;
}
