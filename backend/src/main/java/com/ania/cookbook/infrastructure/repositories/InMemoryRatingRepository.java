package com.ania.cookbook.infrastructure.repositories;
import com.ania.cookbook.domain.model.Rating;
import com.ania.cookbook.domain.repositories.rating.RatingRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class InMemoryRatingRepository implements RatingRepository {
    private final Map<UUID, List<Rating>> ratingsByRecipe = new HashMap<>();

    @Override
    public void save(Rating rating) {
        ratingsByRecipe
                .computeIfAbsent(rating.getRecipeId(), id -> new ArrayList<>())
                .add(rating);
    }

    @Override
    public double getAverageRating(UUID recipeId) {
        List<Rating> ratings = ratingsByRecipe.getOrDefault(recipeId, List.of());
        if (ratings.isEmpty()) return 0.0;

        return ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }
}
