package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;

@AllArgsConstructor
@Getter
public class Recipe {
    private final UUID recipeId;
    private final String recipeName;
    private final Category category;
    private final List<Ingredient> ingredients;
    private final String instructions;
    private final Instant created;
    private final int numberOfServings;
    private final List<String> tags;

    @Builder
    private Recipe(UUID recipeId, String recipeName, Category category, List<Ingredient> ingredients, String instructions,int numberOfServings, List<String> tags) {
        if(recipeId==null){throw new RecipeValidationException("Recipe id cannot be null.");}
        this.recipeId = recipeId;
        if(isBlank(recipeName)){throw new RecipeValidationException("Recipe name cannot be null or empty.");}
        this.recipeName = recipeName;
        if(category==null){throw new RecipeValidationException("Recipe category cannot be null.");}
        this.category = category;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        if(isBlank(instructions)){throw new RecipeValidationException("Recipe instructions cannot be null or empty.");}
        this.instructions = instructions;
        this.created = Instant.now();
        if(numberOfServings<0){throw new RecipeValidationException("Recipe number of servings cannot be negative.");}
        this.numberOfServings = (numberOfServings != 0) ? numberOfServings : calculateServings();
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public static Recipe newRecipe(UUID recipeId, String recipeName, Category category,
                                   List<Ingredient> ingredients, String instructions, int numberOfServings, List<String> tags){
        return new Recipe(recipeId, recipeName, category, ingredients, instructions, numberOfServings, tags);
    }

    public int calculateServings() {
    float totalMassInGrams = 0;
    for (Ingredient ingredient : ingredients) {
        totalMassInGrams += ingredient.getAmount() * ingredient.getUnit().toGrams(1);
    }
    return Math.max(1, Math.round(totalMassInGrams / 350));
    }
}

