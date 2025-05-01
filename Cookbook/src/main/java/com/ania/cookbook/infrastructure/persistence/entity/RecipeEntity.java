package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.infrastructure.converters.IngredientsJsonConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Entity
@Table(name = "recipe")
public class Recipe {
        @Id
        @GeneratedValue
        @Column(name="recipe_id", nullable = false)
        private final UUID recipeId;
        @Column(name = "name", nullable = false)
        private final String recipeName;
        @Enumerated(EnumType.STRING)
        @Column(name = "category", nullable = false)
        private final Category category;
        @Column(name = "ingredients", columnDefinition = "TEXT")
        @Convert(converter = IngredientsJsonConverter.class)
        private final List<Ingredient> ingredients;
        @Column(name = "instructions", nullable = false)
        private final String instructions;
        @Column(name = "created", nullable = false, updatable = false)
        private final Instant created;
        @Column(name = "number_of_servings", nullable = false)
        private final int numberOfServings;
        @Column(name = "tags", nullable = true, columnDefinition = "TEXT")
        private List<String> tags;

    public Recipe() {
        this.recipeId = UUID.randomUUID();
        this.recipeName = null;
        this.category = null;
        this.ingredients = new ArrayList<>();
        this.instructions = null;
        this.created = Instant.now();
        this.numberOfServings = 0;
        this.tags = new ArrayList<>();
    }
        private Recipe(UUID recipeId, String recipeName, Category category, List<Ingredient> ingredients, String instructions, int numberOfServings, List<String> tags) {
            if(recipeId==null){throw new IllegalArgumentException("Recipe id cannot be null");}
            this.recipeId = recipeId;
            if(recipeName.isBlank()){throw new IllegalArgumentException("Recipe name cannot be null or empty");}
            this.recipeName = recipeName;
            this.category = category;
            this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
            if(instructions.isBlank()){throw new IllegalArgumentException("Recipe instructions cannot be null or empty");}
            this.instructions = instructions;
            this.created = Instant.now();
            if(numberOfServings<0){throw new IllegalArgumentException("Recipe number of servings cannot be negative");}
            this.numberOfServings = numberOfServings;
            this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        }

        public static Recipe newRecipe(UUID recipeId, String name, Category category, List<Ingredient> ingredients, String instructions, int numberOfServings, List<String> tags){
            return new Recipe(recipeId, name, category, ingredients, instructions,numberOfServings, tags);
        }


}
