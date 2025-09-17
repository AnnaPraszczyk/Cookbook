package com.ania.cookbook.domain.repositories.image;
import com.ania.cookbook.domain.model.RecipeImage;
import java.util.List;
import java.util.UUID;

public interface RecipeImageRepository {
    void save(RecipeImage image);
    List<RecipeImage> findByRecipeId(UUID recipeId);
    void delete(UUID imageId);
}
