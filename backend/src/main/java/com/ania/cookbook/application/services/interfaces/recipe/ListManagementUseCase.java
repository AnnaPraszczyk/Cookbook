package com.ania.cookbook.application.services.interfaces.recipe;

import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.web.recipe.RecipeListResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;

public interface ListManagementUseCase {

    void createRecipeList(ListName name, String description);
    RecipeListEntry addRecipeToList(UUID recipeId, ListName listName);
    List<Recipe> getRecipesList(ListName list);
    List<RecipeListEntry> getRecipesListEntries(ListName list);
    RecipeListResponse getRecipeListResponse(ListName listName);
    void removeRecipeFromList(UUID entryId, ListName listName);
    boolean clearRecipeList(ListName listName, boolean confirm);
    void deleteRecipeList(ListName listName);
    Map<String, Float> generateShoppingList(ListName list);
    List<ListName> getAllLists();
    record ListName(String name){
        public ListName{
            if(isBlank(name)){
                throw new ListValidationException("List name cannot be null or empty.");
            }
        }
    }

}
