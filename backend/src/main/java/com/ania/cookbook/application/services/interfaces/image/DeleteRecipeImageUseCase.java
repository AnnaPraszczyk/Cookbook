package com.ania.cookbook.application.services.interfaces.image;
import java.util.UUID;

public interface DeleteRecipeImageUseCase {
    void deleteImage(UUID imageId);
}
