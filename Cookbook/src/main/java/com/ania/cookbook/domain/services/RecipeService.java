package com.ania.cookbook.domain.services;

import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

    @Service
    public class RecipeService {

        private final SaveRecipe recipeRepository;
        private final ReadRecipe recipeRepository2;

        @Autowired
        public RecipeService(InMemoryRecipeRepository recipeRepository, ReadRecipe recipeRepository2) {
            this.recipeRepository = recipeRepository;
            this.recipeRepository2 = recipeRepository2;
        }

        public RecipeEntity saveRecipe(RecipeEntity recipe) {
            Optional<RecipeEntity> recipeOptional = recipeRepository2.findRecipeById(recipe.getRecipeId());
            if (recipeOptional.isPresent()) {
                throw new IllegalArgumentException("A recipe with the given ID already exists.");
            }
            Optional<RecipeEntity> existingRecipeByName = recipeRepository2.findRecipeByName(recipe.getRecipeName());
            if(existingRecipeByName.isPresent()){
                throw new IllegalArgumentException("A recipe with the given name already exists.");
            }
            recipeRepository.saveRecipe(recipe);
            return recipe;
        }


        public RecipeEntity updateRecipe(RecipeEntity recipe) {
            if (!recipeRepository.findRecipeById(recipe.getRecipeId()).isPresent()) {
                throw new IllegalArgumentException("Unable to update the recipe because it does not exist."
                );
            }
            return recipeRepository.updateRecipe(recipe);
        }

        public void deleteRecipeById(UUID id) {
            if (!recipeRepository.findRecipeById(id).isPresent()) {
                throw new IllegalArgumentException("Unable to delete the recipe because it does not exist.");
            }
            recipeRepository.deleteRecipeById(id);
        }
    }

