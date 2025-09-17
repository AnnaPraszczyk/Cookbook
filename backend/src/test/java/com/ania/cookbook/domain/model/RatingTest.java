package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.RatingValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    @Test
    void createValidRating() {
        UUID ratingId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        int score = 4;
        Rating rating = Rating.builder()
                .ratingId(ratingId)
                .recipeId(recipeId)
                .score(score)
                .build();

        assertEquals(ratingId, rating.getRatingId());
        assertEquals(recipeId, rating.getRecipeId());
        assertEquals(score, rating.getScore());
        assertNotNull(rating.getRatedAt());
    }

    @Test
    void throwExceptionWhenRatingIdIsNull() {
        UUID recipeId = UUID.randomUUID();

        assertThrows(RatingValidationException.class, () ->
                Rating.builder()
                        .ratingId(null)
                        .recipeId(recipeId)
                        .score(3)
                        .build()
        );
    }

    @Test
    void throwExceptionWhenRecipeIdIsNull() {
        UUID ratingId = UUID.randomUUID();

        assertThrows(RatingValidationException.class, () ->
                Rating.builder()
                        .ratingId(ratingId)
                        .recipeId(null)
                        .score(3)
                        .build()
        );
    }

    @Test
    void throwExceptionWhenScoreIsOutOfRange() {
        UUID ratingId = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();

        assertThrows(RatingValidationException.class, () ->
                Rating.builder()
                        .ratingId(ratingId)
                        .recipeId(recipeId)
                        .score(0)
                        .build()
        );

        assertThrows(RatingValidationException.class, () ->
                Rating.builder()
                        .ratingId(ratingId)
                        .recipeId(recipeId)
                        .score(6)
                        .build()
        );
    }





}