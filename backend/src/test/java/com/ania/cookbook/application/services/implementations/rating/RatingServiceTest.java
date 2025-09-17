package com.ania.cookbook.application.services.implementations.rating;
import com.ania.cookbook.domain.exceptions.RatingValidationException;
import com.ania.cookbook.infrastructure.repositories.InMemoryRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RatingServiceTest {
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        InMemoryRatingRepository ratingRepository = new InMemoryRatingRepository();
        ratingService = new RatingService(ratingRepository);
    }

    @Test
    void addRatingAndCalculateAverage() {
        UUID recipeId = UUID.randomUUID();
        ratingService.addRating(recipeId, 5);
        ratingService.addRating(recipeId, 3);
        ratingService.addRating(recipeId, 4);
        double average = ratingService.getAverageRating(recipeId);

        assertEquals(4.0, average);
    }

    @Test
    void returnZeroWhenNoRatingsExist() {
        UUID recipeId = UUID.randomUUID();
        double average = ratingService.getAverageRating(recipeId);

        assertEquals(0.0, average);
    }

    @Test
    void storeRatingsSeparatelyForDifferentRecipes() {
        UUID recipeA = UUID.randomUUID();
        UUID recipeB = UUID.randomUUID();
        ratingService.addRating(recipeA, 5);
        ratingService.addRating(recipeB, 2);

        assertEquals(5.0, ratingService.getAverageRating(recipeA));
        assertEquals(2.0, ratingService.getAverageRating(recipeB));
    }

    @Test
    void throwExceptionForInvalidScore() {
        UUID recipeId = UUID.randomUUID();

        assertThrows(RatingValidationException.class, () ->
                ratingService.addRating(recipeId, 0)
        );
    }
}