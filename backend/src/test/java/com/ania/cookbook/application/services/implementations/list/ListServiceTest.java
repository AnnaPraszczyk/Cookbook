package com.ania.cookbook.application.services.implementations.list;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryEntryRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryListRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ListServiceTest {
    private ListService listService;
    private InMemoryRecipeRepository recipeRepository;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        recipeRepository = new InMemoryRecipeRepository();
        InMemoryListRepository listRepository = new InMemoryListRepository();
        InMemoryEntryRepository entryRepository = new InMemoryEntryRepository();

        listService = new ListService(
                listRepository, listRepository, listRepository,
                entryRepository, entryRepository, entryRepository,
                recipeRepository
        );
    }

    @Test
    void createNewRecipeList() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "Description", 2);
        SavedList savedList = listService.getRecipesList(listName);

        assertEquals(listName, savedList.getListName());
        assertEquals("Description", savedList.getListDescription());
        assertEquals(2, savedList.getExpectedPortions());
    }

    @Test
    void throwExceptionWhenExpectedPortionsIsZero() {
        ListName listName = new ListName("Lunch");

        assertThrows(ListValidationException.class, () ->
                listService.createRecipeList(listName, "Quick lunch", 0)
        );
    }

    @Test
    void throwExceptionWhenRecipeListDoesNotExist() {
        ListName listName = new ListName("NonExisting");

        assertThrows(ListNotFoundException.class, () ->
                listService.getRecipesList(listName)
        );
    }

    @Test
    void notAllowDuplicateListNames() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "First", 2);

        assertThrows(ListValidationException.class, () ->
                listService.createRecipeList(listName, "Second", 3)
        );
    }

    @Test
    void createListWithEmptyDescription() {
        ListName listName = new ListName("Simple");
        listService.createRecipeList(listName, "", 1);
        SavedList savedList = listService.getRecipesList(listName);

        assertEquals("", savedList.getListDescription());
    }

    @Test
    void createListWithLargeExpectedPortions() {
        ListName listName = new ListName("Feast");
        listService.createRecipeList(listName, "Big party", 1000);
        SavedList savedList = listService.getRecipesList(listName);

        assertEquals(1000, savedList.getExpectedPortions());
    }

    @Test
    void addRecipeToList() {
        ListName listName = new ListName("Lunch");
        listService.createRecipeList(listName, "Lunch", 1);
        UUID product1Id = UUID.randomUUID();
        Product product1 = Product.newProduct(product1Id, new ProductName("Pasta"));
        productRepository.saveProduct(product1);
        Ingredient ingredient1 = Ingredient.newIngredient(product1, 100, Unit.G);
        UUID product2Id = UUID.randomUUID();
        Product product2 = Product.newProduct(product2Id, new ProductName("Cheese"));
        productRepository.saveProduct(product2);
        Ingredient ingredient2 = Ingredient.newIngredient(product2, 50, Unit.G);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Pasta")
                .numberOfServings(2)
                .instructions("Cook")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(ingredient1, ingredient2))
                .tags(List.of("Vegetarian"))
                .build();
        recipeRepository.saveRecipe(recipe);

        ListEntry entry = listService.addRecipeToList(recipeId, listName, 3);
        assertEquals(recipeId, entry.getRecipe().getRecipeId());
        assertEquals(3, entry.getPortions());
    }

    @Test
    void returnSavedListWithEntries() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "Dinner list", 2);
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Pasta"));
        productRepository.saveProduct(product);
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Pasta witch cheese")
                .category(Category.PASTA)
                .ingredients(List.of(Ingredient.newIngredient(product, 100, Unit.G)))
                .instructions("Cook pasta in water and cheese.")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();
        recipeRepository.saveRecipe(recipe);
        listService.addRecipeToList(recipe.getRecipeId(), listName, 2);
        SavedList result = listService.getRecipesList(listName);

        assertEquals(listName, result.getListName());
        assertEquals(1, result.getEntries().size());
        assertEquals(recipe.getRecipeId(), result.getEntries().getFirst().getRecipe().getRecipeId());
    }

    @Test
    void returnListEntriesForExistingList() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "Evening dinner", 1);
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Cheese"));
        productRepository.saveProduct(product);
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Sandwich with cheese")
                .category(Category.SNACK)
                .ingredients(List.of(Ingredient.newIngredient(product, 50, Unit.G)))
                .instructions("Take bread and cheese")
                .numberOfServings(1)
                .tags(List.of("sandwich", "fast", "easy"))
                .build();
        recipeRepository.saveRecipe(recipe);
        listService.addRecipeToList(recipe.getRecipeId(), listName, 1);
        List<ListEntry> entries = listService.getRecipesListEntries(listName);

        assertEquals(1, entries.size());
        assertEquals(recipe.getRecipeId(), entries.getFirst().getRecipe().getRecipeId());
    }

    @Test
    void returnAllCreatedLists() {
        ListName list1 = new ListName("Breakfast");
        ListName list2 = new ListName("Dinner");
        listService.createRecipeList(list1, "Description", 1);
        listService.createRecipeList(list2, "Description", 2);
        List<ListName> allLists = listService.getAllLists();

        assertTrue(allLists.contains(list1));
        assertTrue(allLists.contains(list2));
        assertEquals(2, allLists.size());
    }

    @Test
    void updateRecipeEntry() {
        ListName listName = new ListName("Breakfast");
        listService.createRecipeList(listName, "Breakfast", 1);
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, new ProductName("Eggs"));
        productRepository.saveProduct(product);
        Ingredient ingredient = Ingredient.newIngredient(product, 2, Unit.PC);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Eggs")
                .category(Category.MAIN_COURSE)
                .instructions("Cook")
                .numberOfServings(1)
                .ingredients(List.of(ingredient))
                .tags(List.of("Vegetarian"))
                .build();
        recipeRepository.saveRecipe(recipe);
        ListEntry entry = listService.addRecipeToList(recipeId, listName, 1);
        ListEntry updated = listService.updateRecipeEntry(entry.getEntryId(), 4);

        assertEquals(4, updated.getPortions());
    }

    @Test
    void removeRecipeFromList() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "Dinner", 1);
        UUID productId = UUID.randomUUID();
        Product product = Product.newProduct(productId, new ProductName("Water"));
        productRepository.saveProduct(product);
        Ingredient ingredient = Ingredient.newIngredient(product, 500, Unit.ML);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Soup")
                .instructions("Boil")
                .category(Category.SOUP)
                .numberOfServings(1)
                .ingredients(List.of(ingredient))
                .tags(List.of("Vegetarian"))
                .build();
        recipeRepository.saveRecipe(recipe);
        ListEntry entry = listService.addRecipeToList(recipeId, listName, 1);
        listService.removeRecipeFromList(entry.getEntryId(), listName);
        List<ListEntry> entries = listService.getRecipesListEntries(listName);

        assertTrue(entries.isEmpty());
    }

    @Test
    void generateShoppingList() {
        ListName listName = new ListName("Snack");
        listService.createRecipeList(listName, "Snacks", 1);
        UUID product1Id = UUID.randomUUID();
        Product product1 = Product.newProduct(product1Id, new ProductName("Bread"));
        productRepository.saveProduct(product1);
        Ingredient ingredient1 = Ingredient.newIngredient(product1, 2, Unit.SL);
        UUID product2Id = UUID.randomUUID();
        Product product2 = Product.newProduct(product2Id, new ProductName("Ham"));
        productRepository.saveProduct(product2);
        Ingredient ingredient2 = Ingredient.newIngredient(product2, 50, Unit.G);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.builder()
                .recipeId(recipeId)
                .recipeName("Sandwich")
                .category(Category.SNACK)
                .instructions("Cook")
                .numberOfServings(1)
                .ingredients(List.of(ingredient1, ingredient2))
                .tags(List.of("Vegetarian"))
                .build();
        recipeRepository.saveRecipe(recipe);
        listService.addRecipeToList(recipeId, listName, 2);
        Map<String, Float> shoppingList = listService.generateShoppingList(listName);

        assertEquals(100, shoppingList.get("Bread"));
        assertEquals(100, shoppingList.get("Ham"));
    }

    @Test
    void sumSameProductFromMultipleRecipesInShoppingList() {
        ListName listName = new ListName("Shopping List");
        listService.createRecipeList(listName, "Adding products from different recipes", 1);
        Product tomato = Product.newProduct(UUID.randomUUID(), new ProductName("Tomato"));
        productRepository.saveProduct(tomato);
        Ingredient tomato100g = Ingredient.newIngredient(tomato, 100, Unit.G);
        Ingredient tomato200g = Ingredient.newIngredient(tomato, 200, Unit.G);
        Recipe recipe1 = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Salad 1")
                .category(Category.SALAD)
                .ingredients(List.of(tomato100g))
                .instructions("Cut tomatoes.")
                .numberOfServings(1)
                .tags(List.of())
                .build();
        Recipe recipe2 = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Salad 2")
                .category(Category.SALAD)
                .ingredients(List.of(tomato200g))
                .instructions("Cut more tomatoes.")
                .numberOfServings(2)
                .tags(List.of())
                .build();
        recipeRepository.saveRecipe(recipe1);
        recipeRepository.saveRecipe(recipe2);
        listService.addRecipeToList(recipe1.getRecipeId(), listName, 1);
        listService.addRecipeToList(recipe2.getRecipeId(), listName, 2);
        Map<String, Float> shoppingList = listService.generateShoppingList(listName);

        assertEquals(1, shoppingList.size());
        assertEquals(300f, shoppingList.get("Tomato"));
    }

    @Test
    void clearRecipeListWhenConfirmed() {
        ListName listName = new ListName("ToClear");
        listService.createRecipeList(listName, "List to clear", 1);
        Product product = Product.newProduct(UUID.randomUUID(), new ProductName("Pasta"));
        productRepository.saveProduct(product);
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Pasta witch mushrooms")
                .category(Category.PASTA)
                .ingredients(List.of(Ingredient.newIngredient(product, 100, Unit.G)))
                .instructions("Cook pasta in water and mushrooms.")
                .numberOfServings(1)
                .tags(List.of())
                .build();
        recipeRepository.saveRecipe(recipe);
        listService.addRecipeToList(recipe.getRecipeId(), listName, 1);
        boolean result = listService.clearRecipeList(listName, true);

        assertTrue(result);
        List<ListEntry> entries = listService.getRecipesListEntries(listName);
        assertTrue(entries.isEmpty());
    }

    @Test
    void notClearRecipeListWhenNotConfirmed() {
        ListName listName = new ListName("List");
        listService.createRecipeList(listName, "Don't clear", 1);
        boolean result = listService.clearRecipeList(listName, false);

        assertFalse(result);
    }

    @Test
    void deleteRecipeList() {
        ListName listName = new ListName("List");
        listService.createRecipeList(listName, "List to delete", 1);
        listService.deleteRecipeList(listName);

        assertThrows(ListNotFoundException.class, () ->
                listService.getRecipesList(listName)
        );
    }

    @Test
    void returnTrueWhenRecipeExistsInAnyList() {
        ListName listName = new ListName("Dinner");
        listService.createRecipeList(listName, "Dinner list", 2);
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = Recipe.newRecipe(
                recipeId,
                "Pasta with cheese",
                Category.MAIN_COURSE,
                List.of(),
                "Cook pasta in water and cheese.",
                2,
                List.of("fast", "easy")
        );
        recipeRepository.saveRecipe(recipe);
        listService.addRecipeToList(recipe.getRecipeId(), listName, 2);
        boolean exists = listService.existsRecipeOnListByRecipeId(recipeId);

        assertTrue(exists);
    }

    @Test
    void returnFalseWhenRecipeDoesNotExistInAnyList() {
        UUID recipeId = UUID.randomUUID();
        boolean exists = listService.existsRecipeOnListByRecipeId(recipeId);

        assertFalse(exists);
    }
}