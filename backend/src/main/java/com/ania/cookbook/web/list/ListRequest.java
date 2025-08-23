package com.ania.cookbook.web.list;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRequest {
    private UUID recipeId;
    @NotBlank(message = "List name cannot be blank")
    private String listName;
    private Boolean confirm;
    @Min(value = 1, message = "Portions must be at least 1")
    private Integer portions;
    @Size(max= 255, message = "Description cannot exceed 255 characters")
    private String listDescription;
}
