package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.RatingValidationException;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Rating {
    private final UUID ratingId;
    private final UUID recipeId;
    private final int score;
    private final Instant ratedAt;

    @Builder
    private Rating(UUID ratingId, UUID recipeId, int score) {
        if (ratingId == null) {
            throw new RatingValidationException("Rating id cannot be null.");}
        this.ratingId = ratingId;
        if (recipeId == null) {
            throw new RatingValidationException("Recipe id cannot be null.");}
        this.recipeId = recipeId;
        if (score < 1 || score > 5) {
            throw new RatingValidationException("Rating must be between 1 and 5.");}
        this.score = score;
        this.ratedAt = Instant.now();
    }
}
