package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class RecipeService {
        private final SaveRecipe saveRecipeRepository;
        private final ReadRecipe readRecipeRepository;
        private final UpdateRecipe updateRecipeRepository;
        private final DeleteRecipe deleteRecipeRepository;

        public Recipe saveRecipe(Recipe recipe) {
            if(recipe == null){
                throw new RecipeValidationException("Recipe cannot be null");
            }
            if (readRecipeRepository.existsRecipeById(recipe.getRecipeId())) {
                throw new RecipeValidationException("A recipe with the given Id already exists.");
            }
            if(readRecipeRepository.existsRecipeByName(recipe.getRecipeName())){
                throw new RecipeValidationException("A recipe with the given name already exists.");
            }
            return saveRecipeRepository.saveRecipe(recipe);
        }
        private void verifyRecipeExists(UUID recipeId) {
            if (!readRecipeRepository.existsRecipeById(recipeId)) {
                throw new RecipeNotFoundException("Recipe with ID " + recipeId + " does not exist.");
            }
        }
        public Recipe updateRecipe(Recipe recipe) {
            verifyRecipeExists(recipe.getRecipeId());
            return updateRecipeRepository.updateRecipe(recipe);
        }

        public void deleteRecipeById(UUID id) {
            verifyRecipeExists(id);
            deleteRecipeRepository.deleteRecipeById(id);
        }

        public static RecipeEntity toEntity(Recipe recipe) {
            if (recipe == null) {throw new RecipeValidationException("Recipe cannot be null");}
            return RecipeEntity.newRecipeEntity(recipe.getRecipeId(), recipe.getRecipeName(), recipe.getCategory(),
                    recipe.getIngredients(), recipe.getInstructions(), recipe.getNumberOfServings(), recipe.getTags());
        }

        public static Recipe toDomain(RecipeEntity recipeEntity) {
            if (recipeEntity == null) {throw new RecipeValidationException("RecipeEntity cannot be null");}
            return Recipe.newRecipe(recipeEntity.getRecipeId(), recipeEntity.getRecipeName(), recipeEntity.getCategory(),
                    recipeEntity.getIngredients(), recipeEntity.getInstructions(), recipeEntity.getNumberOfServings(),
                    recipeEntity.getTags());
        }
    }

