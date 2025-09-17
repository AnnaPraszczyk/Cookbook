package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.FavoriteValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {
    @Test
    void createValidFavorite() {
        UUID userId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();

        Favorite favorite = Favorite.builder()
                .userId(userId)
                .recipeId(recipeId)
                .build();

        assertEquals(userId, favorite.getUserId());
        assertEquals(recipeId, favorite.getRecipeId());
        assertNotNull(favorite.getAddedAt());
    }

    @Test
    void throwExceptionWhenUserIdIsNull() {
        UUID recipeId = UUID.randomUUID();

        assertThrows(FavoriteValidationException.class, () ->
                Favorite.builder()
                        .userId(null)
                        .recipeId(recipeId)
                        .build()
        );
    }

    @Test
    void throwExceptionWhenRecipeIdIsNull() {
        UUID userId = UUID.randomUUID();

        assertThrows(FavoriteValidationException.class, () ->
                Favorite.builder()
                        .userId(userId)
                        .recipeId(null)
                        .build()
        );
    }
}