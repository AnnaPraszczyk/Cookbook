package com.ania.cookbook.application.services.implementations.favorite;
import com.ania.cookbook.infrastructure.repositories.InMemoryFavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        InMemoryFavoriteRepository favoriteRepository = new InMemoryFavoriteRepository();
        favoriteService = new FavoriteService(favoriteRepository);
    }

    @Test
    void addRecipeToFavorites() {
        UUID userId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        favoriteService.addToFavorites(userId, recipeId);
        List<UUID> favorites = favoriteService.getFavoriteRecipes(userId);

        assertEquals(1, favorites.size());
        assertTrue(favorites.contains(recipeId));
    }

    @Test
    void removeRecipeFromFavorites() {
        UUID userId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        favoriteService.addToFavorites(userId, recipeId);
        favoriteService.removeFromFavorites(userId, recipeId);
        List<UUID> favorites = favoriteService.getFavoriteRecipes(userId);

        assertFalse(favorites.contains(recipeId));
        assertEquals(0, favorites.size());
    }

    @Test
    void returnEmptyListWhenUserHasNoFavorites() {
        UUID userId = UUID.randomUUID();
        List<UUID> favorites = favoriteService.getFavoriteRecipes(userId);

        assertTrue(favorites.isEmpty());
    }

    @Test
    void handleMultipleFavoritesForUser() {
        UUID userId = UUID.randomUUID();
        UUID recipe1 = UUID.randomUUID();
        UUID recipe2 = UUID.randomUUID();
        favoriteService.addToFavorites(userId, recipe1);
        favoriteService.addToFavorites(userId, recipe2);
        List<UUID> favorites = favoriteService.getFavoriteRecipes(userId);

        assertEquals(2, favorites.size());
        assertTrue(favorites.contains(recipe1));
        assertTrue(favorites.contains(recipe2));
    }

    @Test
    void notFailWhenRemovingNonExistingFavorite() {
        UUID userId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        favoriteService.removeFromFavorites(userId, recipeId);
        List<UUID> favorites = favoriteService.getFavoriteRecipes(userId);

        assertTrue(favorites.isEmpty());
    }
}