package com.ania.cookbook.application.services.interfaces.image;
import java.util.List;
import java.util.UUID;

public interface GetRecipeImagesUseCase {
    List<String> getImages(UUID recipeId);
}
