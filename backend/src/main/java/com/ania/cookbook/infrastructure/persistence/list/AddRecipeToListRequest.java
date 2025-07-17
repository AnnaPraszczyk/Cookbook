package com.ania.cookbook.infrastructure.persistence.list;

import java.util.UUID;

public record AddRecipeToListRequest(UUID recipeId, int portions) {
}
