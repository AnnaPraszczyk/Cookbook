package com.ania.cookbook.infrastructure.persistence.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class RatingEntity {
    @Id
    //@Column(name="ratingId", nullable = false, updatable=false)
    private UUID ratingId;
    //@Column(name="recipeId", nullable = false, updatable=false)
    private UUID recipeId;
   //@Column(name="score", nullable = false, updatable=false)
    private int score;
    //@Column(name="ratedAt", nullable = false, updatable=false)
    private Instant ratedAt;
}
