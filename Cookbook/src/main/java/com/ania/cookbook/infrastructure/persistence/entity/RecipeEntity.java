package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.infrastructure.converters.CategoriesJsonConverter;
import com.ania.cookbook.infrastructure.converters.IngredientsJsonConverter;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "recipe")
public class RecipeEntity {
        @Id
        @GeneratedValue
        @Column(name="recipe_id", nullable = false)
        private final UUID recipeId;

        @Column(name = "name", nullable = false)
        private final String recipeName;

        @Enumerated(EnumType.STRING)
        @Column(name = "categories", columnDefinition = "TEXT")
        @Convert(converter = CategoriesJsonConverter.class)
        private final List<Category> categories;

        @Column(name = "ingredients", columnDefinition = "TEXT")
        @Convert(converter = IngredientsJsonConverter.class)
        private final List<Ingredient> ingredients;

        @Column(name = "instructions", nullable = false)
        private final String instructions;

        @Column(name = "created")
        private final Instant created;

        @Column(name = "number_of_servings")
        private final int numberOfServings;

        public RecipeEntity() {
            this.recipeId = UUID.randomUUID();
            this.recipeName = null;
            this.categories = new ArrayList<>();
            this.ingredients = new ArrayList<>();
            this.instructions = null;
            this.created = Instant.now();
            this.numberOfServings = 0;
        }
        private RecipeEntity(UUID recipeId, String recipeName, List<Category> categories, List<Ingredient> ingredients, String instructions, Instant created, int numberOfServings) {
            if(recipeId==null){throw new IllegalArgumentException("Recipe id cannot be null");}
            this.recipeId = recipeId;
            if(recipeName==null || recipeName.isBlank()){throw new IllegalArgumentException("Recipe name cannot be null or empty");}
            this.recipeName = recipeName;
            this.categories = categories != null ? new ArrayList<>(categories) : new ArrayList<>();
            this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
            if(instructions==null || instructions.isBlank()){throw new IllegalArgumentException("Recipe instructions cannot be null or empty");}
            this.instructions = instructions;
            this.created = Instant.now();
            if(numberOfServings<0){throw new IllegalArgumentException("Recipe number of servings cannot be negative");}
            this.numberOfServings = numberOfServings;
        }

        public static RecipeEntity newRecipeEntity(UUID recipeId, String name, List<Category> categories, List<Ingredient> ingredients, String instructions, int numberOfServings){
            return new RecipeEntity(recipeId, name, categories, ingredients, instructions, Instant.now(),numberOfServings);
        }

    @Override
    public String toString() {
        return "RecipeEntity{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", categories=" + categories +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", created=" + created +
                ", numberOfServings=" + numberOfServings +
                '}';
    }

    public UUID getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Instant getCreated() {
        return created;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }
}
