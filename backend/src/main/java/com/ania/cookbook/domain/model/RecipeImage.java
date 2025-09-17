package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.ImageValidationException;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;

@Getter
public class RecipeImage {
    private final UUID imageId;
    private final UUID recipeId;
    private final String imageUrl;
    private final Instant uploadedAt;

    @Builder
    private RecipeImage(UUID imageId, UUID recipeId, String imageUrl) {
        if(imageId==null){
            throw new ImageValidationException("Image id cannot be null.");}
        this.imageId = imageId;
        if(recipeId==null){
            throw new ImageValidationException("Recipe id cannot be null.");
        }
        this.recipeId = recipeId;
        if(isBlank(imageUrl)){
            throw new ImageValidationException("Image url cannot be null.");
        }
        this.imageUrl = imageUrl;
        this.uploadedAt = Instant.now();
    }
}
