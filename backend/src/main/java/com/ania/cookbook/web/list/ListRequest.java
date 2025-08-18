package com.ania.cookbook.web.list;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListRequest {
    private UUID recipeId;
    @NotBlank
    private String listName;
    private Boolean confirm;
    @Min(1)
    private Integer portions;
    private String listDescription;
}
