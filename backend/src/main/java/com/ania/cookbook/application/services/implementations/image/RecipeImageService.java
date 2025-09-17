package com.ania.cookbook.application.services.implementations.image;
import com.ania.cookbook.application.services.interfaces.image.AddRecipeImageUseCase;
import com.ania.cookbook.application.services.interfaces.image.DeleteRecipeImageUseCase;
import com.ania.cookbook.application.services.interfaces.image.GetRecipeImagesUseCase;
import com.ania.cookbook.domain.model.RecipeImage;
import com.ania.cookbook.domain.repositories.image.RecipeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeImageService implements AddRecipeImageUseCase, GetRecipeImagesUseCase, DeleteRecipeImageUseCase {
    private final RecipeImageRepository recipeImageRepository;

    @Override
    public void addImage(UUID recipeId, String imageUrl) {
        RecipeImage image = RecipeImage.builder()
                .imageId(UUID.randomUUID())
                .recipeId(recipeId)
                .imageUrl(imageUrl)
                .build();

        recipeImageRepository.save(image);
    }

    @Override
    public List<String> getImages(UUID recipeId) {
        return recipeImageRepository.findByRecipeId(recipeId).stream()
                .map(RecipeImage::getImageUrl)
                .toList();
    }

    @Override
    public void deleteImage(UUID imageId) {
        recipeImageRepository.delete(imageId);
    }
}
