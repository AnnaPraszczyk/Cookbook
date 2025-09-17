package com.ania.cookbook.infrastructure.repositories;
import com.ania.cookbook.domain.model.Favorite;
import com.ania.cookbook.domain.repositories.fovorite.FavoriteRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class InMemoryFavoriteRepository implements FavoriteRepository {
    private final Map<UUID, Set<UUID>> favoritesByUser = new HashMap<>();

    @Override
    public void save(Favorite favorite) {
        favoritesByUser
                .computeIfAbsent(favorite.getUserId(), id -> new HashSet<>())
                .add(favorite.getRecipeId());
    }

    @Override
    public void delete(UUID userId, UUID recipeId) {
        Set<UUID> favorites = favoritesByUser.get(userId);
        if (favorites != null) {
            favorites.remove(recipeId);
        }
    }

    @Override
    public List<UUID> getFavoriteRecipes(UUID userId) {
        return new ArrayList<>(favoritesByUser.getOrDefault(userId, Set.of()));
    }
}
