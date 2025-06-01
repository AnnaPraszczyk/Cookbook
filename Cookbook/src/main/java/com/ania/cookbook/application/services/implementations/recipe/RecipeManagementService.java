package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase;
import com.ania.cookbook.domain.exceptions.ListNotFoundExeption;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Getter
@RequiredArgsConstructor
@Service
public class RecipeManagementService implements ListManagementUseCase {
    private final SaveRecipe saveRecipeRepository;
    private final ReadRecipe readRecipeRepository;
    private final List<Recipe> recipeList = new ArrayList<>();
    private final Map<String, List<Recipe>> recipeLists = new HashMap<>();

    @Override
    public void createRecipeList(ListName list) {
        recipeLists.put(list.name(), new ArrayList<>());
    }

    @Override
    public void addRecipeToList(UUID recipeId, ListName list) {
        Recipe matchingRecipe = readRecipeRepository.findRecipeById(recipeId).orElseThrow(
                () -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        List<Recipe> recipes = recipeLists.computeIfAbsent(list.name(), k -> new ArrayList<>());
        if (!recipes.contains(matchingRecipe)) {
            recipes.add(matchingRecipe);
        }
    }

    @Override
    public void saveRecipesList(ListName list) {
        List<Recipe> recipes = recipeLists.get(list.name());
        if (recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found in the list.");
        }
        recipeList.forEach(saveRecipeRepository::saveRecipe);
    }

    @Override
    public List<Recipe> getRecipesList(ListName list) {
        return recipeLists.getOrDefault(list.name(), List.of());
    }

    @Override
    public void removeRecipeFromList(UUID recipeId, ListName list) {
        if (!recipeLists.containsKey(list.name())) {
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        } else if (recipeId == null) {
            throw new RecipeValidationException("Recipe ID cannot be null.");
        }
        List<Recipe> recipes = recipeLists.get(list.name());
        if(recipes == null || recipes.isEmpty()){
            throw new RecipeNotFoundException("No recipes found in the list.");
        }
        boolean removed = recipes.removeIf(recipe -> recipe.getRecipeId().equals(recipeId));
        if(!removed){
            throw new RecipeNotFoundException("Recipe with given ID does not exist in the list.");
        }
    }

    @Override
    public boolean clearRecipeList(ListName list, boolean confirm) {
        if (!confirm) {
            return false;
        }
        if (!recipeLists.containsKey(list.name())) {
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        }
        List<Recipe> recipes = recipeLists.get(list.name());
        if(recipes == null){
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        }
            recipes.clear();
            return true;
    }

    @Override
    public void deleteRecipeList(ListName list) {
        if (!recipeLists.containsKey(list.name())) {
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        }
        List<Recipe> removedList = recipeLists.remove(list.name());
        if (removedList == null) {
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        }
    }

    @Override
    public Map<String, Float> generateShoppingList(ListName list) {
        if (!recipeLists.containsKey(list.name())) {
            throw new ListNotFoundExeption("Recipe list with the given name does not exist.");
        }
        Map<String, Float> shoppingList = new HashMap<>();
        List<Recipe> recipes = recipeLists.get(list.name());

        for (Recipe recipe : recipes) {
            Map<String, Float> singleRecipeIngredients = new HashMap<>();

            for (Ingredient ingredient : recipe.getIngredients()) {
                String productName = ingredient.getProduct().getProductName().name();
                float amountInGrams = ingredient.getUnit().toGrams(ingredient.getAmount());

                singleRecipeIngredients.merge(productName, amountInGrams, Float::sum);
            }

            for (Map.Entry<String, Float> entry : singleRecipeIngredients.entrySet()) {
                shoppingList.merge(entry.getKey(), entry.getValue(), Float::sum);
            }
        }
        return shoppingList;
    }
}
