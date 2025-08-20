package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.application.services.interfaces.recipe.DeleteRecipeUseCase.DeleteRecipeCase;
import com.ania.cookbook.application.services.interfaces.recipe.UpdateRecipeUseCase.UpdateRecipeCase;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.infrastructure.repositories.InMemoryEntryRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryListRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import com.ania.cookbook.infrastructure.repositories.InMemoryRecipeRepository;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeServiceTest {
    private InMemoryRecipeRepository recipeRepository;
    private InMemoryEntryRepository entryRepository;
    private InMemoryListRepository listRepository;
    private RecipeService recipeService;
    private ReadRecipeService readRecipeService;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        entryRepository = new InMemoryEntryRepository();
        listRepository = new InMemoryListRepository();

        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductUseCase productUseCase = new ProductService(productRepository, productRepository, productRepository, productRepository);
        recipeService = new RecipeService(recipeRepository, recipeRepository, recipeRepository, recipeRepository, productUseCase, listRepository, entryRepository);
        readRecipeService = new ReadRecipeService(recipeRepository);
    }

    @Test
    void createRecipe() {
        IngredientRequest raw1 = new IngredientRequest("Flour", 20f, Unit.DAG);
        IngredientRequest raw2 = new IngredientRequest("Sugar", 10f, Unit.G);
        CreateRecipe request = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(raw1,raw2), "Mix and fry", 2, List.of("Easy"));
        Recipe savedRecipe = recipeService.createRecipe(request);

        assertNotNull(savedRecipe);
        assertEquals(request.getRecipeName(), savedRecipe.getRecipeName());
        assertEquals(request.getCategory(), savedRecipe.getCategory());
        assertEquals(request.getIngredients().size(), savedRecipe.getIngredients().size());
        assertEquals(request.getInstructions(), savedRecipe.getInstructions());
        for (Ingredient ing : savedRecipe.getIngredients()) {
            assertNotNull(ing.getProduct().getProductId());
            assertNotNull(ing.getProduct().getProductName());
        }
    }

    @Test
    void createRecipeWhenNameIsNull() {
        assertThrows(RecipeValidationException.class, () -> readRecipeService.findRecipeByName(null));
    }

    @Test
    void createRecipeWhenNameIsEmpty() {
        assertThrows(RecipeValidationException.class, () -> readRecipeService.findRecipeByName(""));
    }

    @Test
    void updateRecipe() {
        CreateRecipe createRecipe = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe originalRecipe = recipeService.createRecipe(createRecipe);
        UpdateRecipeCase updateRecipe = new UpdateRecipeCase("Updated Pancakes", null,null,
                "Updated Instructions",5,null );
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRecipe);

        assertEquals("Updated Pancakes", updatedRecipe.getRecipeName());
        assertEquals(originalRecipe.getCategory(), updatedRecipe.getCategory());
        assertEquals(originalRecipe.getIngredients(), updatedRecipe.getIngredients());
        assertEquals("Updated Instructions", updatedRecipe.getInstructions());
        assertEquals(5, updatedRecipe.getNumberOfServings());
        assertEquals(originalRecipe.getTags(), updatedRecipe.getTags());
    }

    @Test
    void updateRecipeWhenNotExist() {
        UpdateRecipeCase request = new UpdateRecipeCase("New Recipe", Category.DESSERT,
                List.of(new IngredientRequest("Cheese", 12, Unit.DAG)),
                "Mix and cook", 2, List.of("Healthy"));

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(UUID.randomUUID(),request));
    }

    @Test
    void updateRecipeWhenNumberOfServingsIsNegative() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 10, Unit.DAG)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe originalRecipe = recipeService.createRecipe(createRequest);
        UpdateRecipeCase updateRequest = new UpdateRecipeCase(
                "Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 200, Unit.G)),
                "Mix ingredients and fry.", -2, List.of("Easy"));
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRequest);

        assertEquals(2, updatedRecipe.getNumberOfServings());
    }

    @Test
    void updateRecipeWhenNumberOfServingsIsCalculate() {
        CreateRecipe createRequest = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 10, Unit.DAG),
                        new IngredientRequest("Eggs", 2, Unit.G)),
                "Mix and cook", 2, List.of("Easy", "Breakfast"));
        Recipe originalRecipe = recipeService.createRecipe(createRequest);
        UpdateRecipeCase updateRequest = new UpdateRecipeCase("Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 700, Unit.G)),
                "Mix ingredients and fry.", 0, List.of("Easy"));
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToUpdate = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        Recipe updatedRecipe = recipeService.updateRecipe(recipeToUpdate.getRecipeId(),updateRequest);

        assertEquals(2, updatedRecipe.getNumberOfServings());
    }

        @Test
    void deleteRecipeById() {
        CreateRecipe createRecipe = new CreateRecipe("Pancakes", Category.DESSERT,
                List.of(new IngredientRequest("Flour", 200, Unit.G)),
                "Mix ingredients and fry.", 4, List.of("Easy"));
        Recipe originalRecipe = recipeService.createRecipe(createRecipe);
        List<Recipe> foundedRecipes =readRecipeService.findRecipeByName("Pancakes");
        Recipe recipeToDelete = recipeService.selectRecipeFromList(foundedRecipes,originalRecipe.getRecipeId());
        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(recipeToDelete.getRecipeId(),recipeToDelete.getRecipeName());
        recipeService.deleteRecipe(deleteRequest);

        assertTrue(recipeRepository.findRecipeById(recipeToDelete.getRecipeId()).isEmpty());
        assertFalse(recipeRepository.existsRecipeById(recipeToDelete.getRecipeId()));
    }

    @Test
    void throwExceptionWhenRecipeIsUsedInList() {
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
        ListName listName = new ListName("List");
        SavedList savedList = new SavedList(listName, Instant.now(), "New list",2,List.of());
        listRepository.save(savedList);
        ListEntry entry = new ListEntry(UUID.randomUUID(), recipe, savedList, 1);
        entryRepository.save(entry);
        DeleteRecipeCase deleteCase = new DeleteRecipeCase(recipeId, "Pasta with cheese");

        assertThrows(RecipeValidationException.class, () -> recipeService.deleteRecipe(deleteCase));
    }

    @Test
    void deleteRecipeWhenNotExist() {
        DeleteRecipeCase deleteRequest = new DeleteRecipeCase(UUID.randomUUID(),"Non Existing Recipe");

        Exception exception = assertThrows(RecipeNotFoundException.class,
                () -> recipeService.deleteRecipe(deleteRequest));
        assertEquals("Recipe with given name does not exist.", exception.getMessage());
    }
}
