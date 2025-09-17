package com.ania.cookbook.application.services.interfaces.favorite;
import java.util.UUID;

public interface AddToFavoritesUseCase {
    void addToFavorites(UUID userId, UUID recipeId);
}
