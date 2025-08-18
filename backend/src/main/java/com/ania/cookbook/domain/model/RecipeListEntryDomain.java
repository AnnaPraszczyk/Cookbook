package com.ania.cookbook.domain.model;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
public class RecipeListEntryDomain {
    private final UUID entryId;
    private final Recipe recipe;
    private final SavedRecipeListDomain savedRecipeList;
    private final int portions;

    @Builder
    public RecipeListEntryDomain(UUID entryId, Recipe recipe,SavedRecipeListDomain savedRecipeList , int portions) {
        if(entryId==null){throw new ListValidationException("Entry id cannot be null.");}
        this.entryId = entryId;
        if(recipe==null){throw new RecipeValidationException("Recipe cannot be null.");}
        this.recipe = recipe;
        if(savedRecipeList==null){throw new ListValidationException("Saved recipe list cannot be null.");}
        this.savedRecipeList = savedRecipeList;
        if (portions < 0) {throw new RecipeValidationException("Portions cannot be negative");}
        this.portions = portions;
    }
}
