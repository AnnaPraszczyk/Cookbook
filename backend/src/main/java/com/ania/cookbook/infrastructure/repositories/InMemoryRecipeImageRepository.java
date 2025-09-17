package com.ania.cookbook.infrastructure.repositories;
import com.ania.cookbook.domain.model.RecipeImage;
import com.ania.cookbook.domain.repositories.image.RecipeImageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryRecipeImageRepository implements RecipeImageRepository {
    private final Map<UUID, List<RecipeImage>> imagesByRecipe = new HashMap<>();
    private final Map<UUID, RecipeImage> imagesById = new HashMap<>();

    @Override
    public void save(RecipeImage image) {
        imagesByRecipe
                .computeIfAbsent(image.getRecipeId(), id -> new ArrayList<>())
                .add(image);
        imagesById.put(image.getImageId(), image);
    }

    @Override
    public List<RecipeImage> findByRecipeId(UUID recipeId) {
        return new ArrayList<>(imagesByRecipe.getOrDefault(recipeId, List.of()));
    }

    @Override
    public void delete(UUID imageId) {
        RecipeImage image = imagesById.remove(imageId);
        if (image != null) {
            List<RecipeImage> images = imagesByRecipe.get(image.getRecipeId());
            if (images != null) {
                images.removeIf(i -> i.getImageId().equals(imageId));
            }
        }
    }


}
