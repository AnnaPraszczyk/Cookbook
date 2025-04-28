package com.ania.cookbook.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipe {
        private final UUID recipeId;
        private final String recipeName;
        private final List<Category> categories;
        private final List<Ingredient> ingredients;
        private final String instructions;
        private final Instant created;
        private final int numberOfServings;

        public Recipe() {
            this.recipeId = UUID.randomUUID();
            this.recipeName = null;
            this.categories = new ArrayList<>();
            this.ingredients = new ArrayList<>();
            this.created = Instant.now();
            this.instructions = null;
            this.numberOfServings = 0;
        }
    private Recipe(UUID recipe_id, String recipeName, List<Category> categories, List<Ingredient> ingredients, String instructions, Instant created, int numberOfServings) {
        if(recipe_id==null){throw new IllegalArgumentException("Recipe id cannot be null");}
        this.recipeId = recipe_id;
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

    public static Recipe newRecipe(UUID recipeId, String name, List<Category> categories, List<Ingredient> ingredients, String instructions, int numberOfServings){
            return new Recipe(recipeId, name, categories, ingredients, instructions, Instant.now(),numberOfServings);
    }

    @Override
    public String toString() {
        return "Recipe{" +
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

    public String getInstructions() {
        return instructions;
    }

    public Instant getCreated() {
        return created;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }
}
