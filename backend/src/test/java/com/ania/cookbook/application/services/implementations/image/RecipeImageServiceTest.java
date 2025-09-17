package com.ania.cookbook.application.services.implementations.image;
import com.ania.cookbook.domain.model.RecipeImage;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeImageServiceTest {
    private RecipeImageService service;
    private InMemoryRecipeImageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRecipeImageRepository();
        service = new RecipeImageService(repository);
    }

    @Test
    void addImageToRecipe() {
        UUID recipeId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.jpg";
        service.addImage(recipeId, imageUrl);
        List<String> images = service.getImages(recipeId);

        assertEquals(1, images.size());
        assertEquals(imageUrl, images.getFirst());
    }

    @Test
    void returnMultipleImagesForRecipe() {
        UUID recipeId = UUID.randomUUID();
        service.addImage(recipeId, "https://example.com/1.jpg");
        service.addImage(recipeId, "https://example.com/2.jpg");
        List<String> images = service.getImages(recipeId);

        assertEquals(2, images.size());
        assertTrue(images.contains("https://example.com/1.jpg"));
        assertTrue(images.contains("https://example.com/2.jpg"));
    }

    @Test
    void deleteImageById() {
        UUID recipeId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.jpg";
        RecipeImage image = RecipeImage.builder()
                .imageId(UUID.randomUUID())
                .recipeId(recipeId)
                .imageUrl(imageUrl)
                .build();
        repository.save(image);
        service.deleteImage(image.getImageId());
        List<String> images = service.getImages(recipeId);

        assertTrue(images.isEmpty());
    }

    @Test
    void returnEmptyListWhenNoImagesExist() {
        UUID recipeId = UUID.randomUUID();
        List<String> images = service.getImages(recipeId);

        assertTrue(images.isEmpty());
    }
}