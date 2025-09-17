package com.ania.cookbook.application.services.interfaces.favorite;
import java.util.List;
import java.util.UUID;

public interface GetFavoriteRecipesUseCase {
    List<UUID> getFavoriteRecipes(UUID userId);

}
