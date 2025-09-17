package com.ania.cookbook.application.services.interfaces.favorite;
import java.util.UUID;

public interface RemoveFromFavoritesUseCase {
    void removeFromFavorites(UUID userId, UUID recipeId);
}
