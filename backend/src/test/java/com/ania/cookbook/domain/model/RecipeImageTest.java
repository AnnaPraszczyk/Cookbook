package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.ImageValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeImageTest {
    @Test
    void createValidRecipeImage() {
        UUID imageId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.jpg";
        RecipeImage image = RecipeImage.builder()
                .imageId(imageId)
                .recipeId(recipeId)
                .imageUrl(imageUrl)
                .build();

        assertEquals(imageId, image.getImageId());
        assertEquals(recipeId, image.getRecipeId());
        assertEquals(imageUrl, image.getImageUrl());
        assertNotNull(image.getUploadedAt());
    }

    @Test
    void throwExceptionWhenImageIdIsNull() {
        UUID recipeId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.jpg";

        assertThrows(ImageValidationException.class, () ->
                RecipeImage.builder()
                        .imageId(null)
                        .recipeId(recipeId)
                        .imageUrl(imageUrl)
                        .build()
        );
    }

    @Test
    void throwExceptionWhenRecipeIdIsNull() {
        UUID imageId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.jpg";

        assertThrows(ImageValidationException.class, () ->
                RecipeImage.builder()
                        .imageId(imageId)
                        .recipeId(null)
                        .imageUrl(imageUrl)
                        .build()
        );
    }

    @Test
    void throwExceptionWhenImageUrlIsBlank() {
        UUID imageId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();

        assertThrows(ImageValidationException.class, () ->
                RecipeImage.builder()
                        .imageId(imageId)
                        .recipeId(recipeId)
                        .imageUrl(" ")
                        .build()
        );
    }
}