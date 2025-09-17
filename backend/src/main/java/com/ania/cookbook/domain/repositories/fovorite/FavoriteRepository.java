package com.ania.cookbook.domain.repositories.fovorite;
import com.ania.cookbook.domain.model.Favorite;
import java.util.List;
import java.util.UUID;

public interface FavoriteRepository {
    void save(Favorite favorite);
    void delete(UUID userId, UUID recipeId);
    List<UUID> getFavoriteRecipes(UUID userId);
}
