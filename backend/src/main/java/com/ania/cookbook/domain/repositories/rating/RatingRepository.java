package com.ania.cookbook.domain.repositories.rating;
import com.ania.cookbook.domain.model.Rating;
import java.util.UUID;

public interface RatingRepository {
    void save(Rating rating);
    double getAverageRating(UUID recipeId);
}
