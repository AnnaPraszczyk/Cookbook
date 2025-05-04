package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter

public class Recipe {
        private final UUID recipeId;
        private final String recipeName;
        private final Category category;
        private final List<Ingredient> ingredients;
        private final String instructions;
        private final Instant created;
        private final int numberOfServings;
        private List<String> tags;

        public Recipe() {
            this.recipeId = UUID.randomUUID();
            this.recipeName = null;
            this.category = null;
            this.ingredients = new ArrayList<>();
            this.instructions = null;
            this.created = Instant.now();
            this.numberOfServings = 0;
        }

    private Recipe(UUID recipe_id, String recipeName, Category category, List<Ingredient> ingredients, String instructions, int numberOfServings, List<String> tags) {
        if(recipe_id==null){throw new RecipeValidationException("Recipe id cannot be null");}
        this.recipeId = recipe_id;
        if(recipeName ==null || recipeName.isBlank()){throw new RecipeValidationException("Recipe name cannot be null or empty");}
        this.recipeName = recipeName;
        if(category==null){throw new RecipeValidationException("Recipe category cannot be null");}
        this.category = category;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        if(instructions==null || instructions.isBlank()){throw new RecipeValidationException("Recipe instructions cannot be null or empty");}
        this.instructions = instructions;
        this.created = Instant.now();
        if(numberOfServings<0){throw new RecipeValidationException("Recipe number of servings cannot be negative");}
        this.numberOfServings = numberOfServings;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public static Recipe newRecipe(UUID recipeId, String name, Category category, List<Ingredient> ingredients, String instructions, int numberOfServings, List<String> tags){
            return new Recipe(recipeId, name, category, ingredients, instructions, numberOfServings, tags);
    }
}
