package com.ania.cookbook.application.services.implementations.rating;
import com.ania.cookbook.application.services.interfaces.rating.AddRatingUseCase;
import com.ania.cookbook.application.services.interfaces.rating.GetAverageRatingUseCase;
import com.ania.cookbook.domain.model.Rating;
import com.ania.cookbook.domain.repositories.rating.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService implements AddRatingUseCase, GetAverageRatingUseCase {
    private final RatingRepository ratingRepository;

    @Override
    public void addRating(UUID recipeId, int score) {
        Rating rating = Rating.builder()
                .ratingId(UUID.randomUUID())
                .recipeId(recipeId)
                .score(score)
                .build();

        ratingRepository.save(rating);
    }

    @Override
    public double getAverageRating(UUID recipeId) {
        return ratingRepository.getAverageRating(recipeId);
    }
}
