package com.ania.cookbook.application.services.interfaces.image;
import java.util.UUID;

public interface AddRecipeImageUseCase {
    void addImage(UUID recipeId, String imageUrl);
}
