package com.ania.cookbook.application.services.interfaces.rating;
import java.util.UUID;

public interface AddRatingUseCase {
    void addRating(UUID recipeId, int score);
}
