package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.CreateRecipeUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.list.ReadEntry;
import com.ania.cookbook.domain.repositories.list.ReadList;
import com.ania.cookbook.domain.repositories.recipe.DeleteRecipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.domain.repositories.recipe.UpdateRecipe;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import static io.micrometer.common.util.StringUtils.isBlank;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService implements CreateRecipeUseCase, UpdateRecipeUseCase, DeleteRecipeUseCase {
    private final SaveRecipe saveRecipeRepository;
    private final ReadRecipe readRecipeRepository;
    private final UpdateRecipe updateRecipeRepository;
    private final DeleteRecipe deleteRecipeRepository;
    private final ProductUseCase productUseCase;
    private final ReadList readListRepository;
    private final ReadEntry readEntryRepository;

    @Override
    public Recipe createRecipe(CreateRecipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients().stream()
                .map(i -> {
                    ProductName name = ProductName.from(i.productName());
                    Product product = productUseCase
                            .findProductByName(name)
                            .orElseGet(() -> productUseCase.addProduct(name));
                    return Ingredient.newIngredient(product, i.amount(), i.unit());
                })
                .toList();
        UUID id = UUID.randomUUID();
        Recipe newRecipe = Recipe.newRecipe(id,recipe.getRecipeName(),recipe.getCategory(),ingredients,
                recipe.getInstructions(),recipe.getNumberOfServings(),
                recipe.getTags());
        return saveRecipeRepository.saveRecipe(newRecipe);
    }

    @Override
    public Recipe updateRecipe(UUID recipeId, UpdateRecipeCase recipe) {
        Recipe originalRecipe = readRecipeRepository.findRecipeById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Unable to find the recipe because it does not exist."));
        String updatedName = !isBlank(recipe.name()) ? recipe.name() : originalRecipe.getRecipeName();
        Category updatedCategory = recipe.category() != null ? recipe.category() : originalRecipe.getCategory();
        List<Ingredient> updatedIngredients = recipe.ingredients() != null && !recipe.ingredients().isEmpty()
                ? recipe.ingredients().stream()
                .map(i -> {
                    ProductName name = ProductName.from(i.productName());
                    Product product = productUseCase
                            .findProductByName(name)
                            .orElseGet(() -> productUseCase.addProduct(name));
                    return Ingredient.newIngredient(product, i.amount(), i.unit());
                })
                .toList()
                : originalRecipe.getIngredients();
        String updatedInstructions = !isBlank(recipe.instructions()) ? recipe.instructions() : originalRecipe.getInstructions();
        int updatedNumberOfServings = recipe.numberOfServings() >= 0 ? recipe.numberOfServings() :  originalRecipe.getNumberOfServings();
        List<String> updatedTags = recipe.tags() != null ? recipe.tags() : originalRecipe.getTags();
        Recipe updatedRecipe = Recipe.newRecipe(originalRecipe.getRecipeId(),
                updatedName, updatedCategory, updatedIngredients,
                updatedInstructions, updatedNumberOfServings, updatedTags);
        return updateRecipeRepository.updateRecipe(updatedRecipe);
    }

    public Recipe selectRecipeFromList(List<Recipe> recipes,UUID recipeId) {
        if(recipes ==null || recipes.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found in the list.");
        }
        return recipes.stream()
                .filter(recipe -> recipe.getRecipeId().equals(recipeId))
                .findFirst()
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with given Id not found"));
    }

    @Override
    public void deleteRecipe(DeleteRecipeCase recipe) {
        List<Recipe> matchingRecipes = readRecipeRepository.findRecipeByName(recipe.recipeName());
        if (matchingRecipes.isEmpty()) {
            throw new RecipeNotFoundException("Recipe with given name does not exist.");
        }
        Recipe recipeToDelete = (matchingRecipes.size()>1) ?
            selectRecipeFromList(matchingRecipes, recipe.recipeId()) :
                matchingRecipes.getFirst();
        readRecipeRepository.findRecipeById(recipeToDelete.getRecipeId()).orElseThrow(()
                -> new RecipeNotFoundException("Recipe with given Id not found"));
        boolean isReferenced = readListRepository.getAllLists().stream()
                .anyMatch(listName -> readEntryRepository.findByRecipeIdAndListName(recipe.recipeId(), listName).isPresent()
                );
        if (isReferenced) {
            throw new RecipeValidationException("Cannot delete recipe — it is used in a recipe list: ");
        }
        deleteRecipeRepository.deleteRecipeById(recipeToDelete.getRecipeId());
    }
}

