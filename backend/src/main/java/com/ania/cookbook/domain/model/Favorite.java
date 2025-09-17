package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.FavoriteValidationException;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Favorite {
    private final UUID userId;
    private final UUID recipeId;
    private final Instant addedAt;

    @Builder
    private Favorite(UUID userId, UUID recipeId) {
        if(userId==null){
            throw new FavoriteValidationException("User id cannot be null.");}
        this.userId = userId;
        if(recipeId==null){
            throw new FavoriteValidationException("Recipe id cannot be null.");
        }
        this.recipeId = recipeId;
        this.addedAt = Instant.now();
    }
}
