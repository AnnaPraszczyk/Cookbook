package com.ania.cookbook.application.services.interfaces.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.SavedList;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ListUseCase {

    void createRecipeList(ListName listName, String description, Integer defaultPortions);
    ListEntry addRecipeToList(UUID recipeId, ListName listName, Integer portions);
    List<Recipe> getRecipesList(ListName listName);
    List<ListEntry> getRecipesListEntries(ListName listName);
    SavedList getRecipeList(ListName listName);
    ListEntry updateRecipeEntry(UUID entryId, Integer portions);
    void removeRecipeFromList(UUID entryId, ListName listName);
    boolean clearRecipeList(ListName listName, boolean confirm);
    void deleteRecipeList(ListName listName);
    Map<String, Float> generateShoppingList(ListName listName);
    List<ListName> getAllLists();
}
