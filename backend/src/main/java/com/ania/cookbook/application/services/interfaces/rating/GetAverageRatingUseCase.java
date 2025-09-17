package com.ania.cookbook.application.services.interfaces.rating;
import java.util.UUID;

public interface GetAverageRatingUseCase {
    double getAverageRating(UUID recipeId);
}
