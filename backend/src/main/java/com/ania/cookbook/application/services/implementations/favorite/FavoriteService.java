package com.ania.cookbook.application.services.implementations.favorite;
import com.ania.cookbook.application.services.interfaces.favorite.AddToFavoritesUseCase;
import com.ania.cookbook.application.services.interfaces.favorite.GetFavoriteRecipesUseCase;
import com.ania.cookbook.application.services.interfaces.favorite.RemoveFromFavoritesUseCase;
import com.ania.cookbook.domain.model.Favorite;
import com.ania.cookbook.domain.repositories.fovorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService implements AddToFavoritesUseCase,
        GetFavoriteRecipesUseCase, RemoveFromFavoritesUseCase {
    private final FavoriteRepository favoriteRepository;

    @Override
    public void addToFavorites(UUID userId, UUID recipeId) {
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .recipeId(recipeId)
                .build();
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFromFavorites(UUID userId, UUID recipeId) {
        favoriteRepository.delete(userId, recipeId);
    }

    @Override
    public List<UUID> getFavoriteRecipes(UUID userId) {
        return favoriteRepository.getFavoriteRecipes(userId);
    }
}
