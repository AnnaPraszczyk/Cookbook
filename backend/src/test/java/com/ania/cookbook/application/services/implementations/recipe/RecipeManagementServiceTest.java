package com.ania.cookbook.application.services.implementations.recipe;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListNotFoundException;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeListEntry;
import com.ania.cookbook.infrastructure.persistence.entity.SavedRecipeList;
import com.ania.cookbook.infrastructure.persistence.list.RecipeListEntryRepository;
import com.ania.cookbook.infrastructure.persistence.list.SavedRecipeListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class RecipeManagementServiceTest {
    @Mock
    private ReadRecipe readRecipeRepository;
    @Mock
    private RecipeListEntryRepository entryRepository;
    @Mock
    private SavedRecipeListRepository savedListRepository;
    @Mock
    private RecipeMapper recipeMapper;
    @InjectMocks
    private RecipeManagementService service;

    private ListName listName;
    private SavedRecipeList savedList;

    @BeforeEach
    void setup() {
        listName = new ListName("TestList");
        savedList = SavedRecipeList.builder()
                .listName(listName.name())
                .createdAt(Instant.now())
                .listDescription("")
                .expectedPortions(0)
                .entries(new ArrayList<>())
                .build();
    }
    @Test
    void createNewList() {
        Mockito.when(savedListRepository.existsByListName(listName.name())).thenReturn(false);
        String description = "list description";
        service.createRecipeList(listName, description);

        Mockito.verify(savedListRepository).save(Mockito.argThat(list ->
                list.getListName().equals(savedList.getListName()) &&
                        list.getListDescription().equals(description) &&
                        list.getEntries().isEmpty()
        ));
    }

    @Test
    void createRecipeListWhenNameIsBlank() {
        assertThrows(ListValidationException.class,
                () -> service.createRecipeList(new ListName(""),"List description"));
    }

    @Test
    void createRecipeListWhenNameIsNull() {
        assertThrows(ListValidationException.class,
                () -> service.createRecipeList(new ListName(null),"List description"));
    }

    @Test
    void addRecipeToList() {
        UUID recipeId = UUID.randomUUID();
        ListName list = new ListName("Weekend");
        Recipe recipe = mock(Recipe.class);
        RecipeEntity recipeEntity = mock(RecipeEntity.class);
        SavedRecipeList savedList = SavedRecipeList.builder().listName(list.name()).build();

        Mockito.when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
        Mockito.when(savedListRepository.findByListName(list.name())).thenReturn(Optional.of(savedList));
        Mockito.when(entryRepository.findBySavedList_ListName(list.name())).thenReturn(List.of());
        Mockito.when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
        Mockito.when(recipe.getNumberOfServings()).thenReturn(4);
        service.addRecipeToList(recipeId, list);

        Mockito.verify(entryRepository).save(Mockito.argThat(entry ->
                entry.getRecipe().equals(recipeEntity) &&
                        entry.getSavedList().equals(savedList) &&
                        entry.getPortions() == 4
        ));
    }

    @Test
    void addRecipeByIdWhenRecipeNotFound() {
        UUID fakeRecipeId = UUID.randomUUID();
        ListName listName = new ListName("New List");
        Mockito.when(readRecipeRepository.findRecipeById(fakeRecipeId))
                .thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () ->
                service.addRecipeToList(fakeRecipeId, listName));
    }

    @Test
    void notDuplicateRecipeEntriesInList() {
        ListName listName = new ListName("Desserts");
        SavedRecipeList savedList = SavedRecipeList.builder().listName(listName.name()).build();
        UUID recipeId = UUID.randomUUID();
        IngredientJson ingredient = new IngredientJson(UUID.randomUUID(), "Flour", 200F, Unit.G);
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipeId(recipeId)
                .recipeName("Test")
                .category(Category.DESSERT)
                .ingredients(List.of(ingredient))
                .instructions("Mix")
                .numberOfServings(2)
                .tags(List.of())
                .build();
        Recipe recipe = mock(Recipe.class);
        Mockito.when(recipe.getNumberOfServings()).thenReturn(2);
        Mockito.when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
        Mockito.when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
        Mockito.when(savedListRepository.findByListName(listName.name())).thenReturn(Optional.of(savedList));
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of())
                .thenReturn(List.of(
                        RecipeListEntry.builder()
                                .recipe(recipeEntity)
                                .savedList(savedList)
                                .portions(2)
                                .build()
                ));
        service.addRecipeToList(recipeId, listName);

        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(
                        RecipeListEntry.builder().recipe(recipeEntity).savedList(savedList).portions(2).build()
                ));
        service.addRecipeToList(recipeId, listName);
        service.addRecipeToList(recipeId, listName);
        Mockito.verify(entryRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void returnRecipesFromList() {
        ListName listName = new ListName("Desserts");
        UUID recipeId = UUID.randomUUID();
        Mockito.when(savedListRepository.existsById(listName.name())).thenReturn(true);
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipeId(recipeId)
                .recipeName("Test")
                .category(Category.DESSERT)
                .instructions("Mix")
                .numberOfServings(2)
                .ingredients(List.of())
                .tags(List.of())
                .build();
        Recipe recipe = mock(Recipe.class);
        Mockito.when(recipeMapper.toDomain(recipeEntity)).thenReturn(recipe);
        Mockito.when(recipe.getRecipeName()).thenReturn("Pancakes");
        SavedRecipeList savedList = SavedRecipeList.builder()
                .listName(listName.name())
                .build();
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(recipeEntity)
                .savedList(savedList)
                .portions(2)
                .build();
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry));
        List<Recipe> result = service.getRecipesList(listName);

        assertEquals(1, result.size());
        assertEquals("Pancakes", result.getFirst().getRecipeName());
    }

    @Test
    void throwsExceptionWhenRecipeListDoesNotExist() {
        ListName listName = new ListName("NonExistingList");
        Mockito.when(savedListRepository.existsById(listName.name())).thenReturn(false);

        assertThrows(ListNotFoundException.class, () -> {service.getRecipesList(listName);});
    }

    @Test
    void returnsEmptyListWhenNoEntriesFoundInExistingList() {
        ListName listName = new ListName("EmptyList");
        Mockito.when(savedListRepository.existsById(listName.name())).thenReturn(true);
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name())).thenReturn(List.of());
        List<Recipe> result = service.getRecipesList(listName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeRecipeFromList() {
        UUID recipeId = UUID.randomUUID();
        ListName listName = new ListName("Desserts");
        SavedRecipeList savedList = SavedRecipeList.builder().listName(listName.name()).build();
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .recipeId(recipeId)
                .recipeName("Test")
                .category(Category.DESSERT)
                .instructions("Mix")
                .numberOfServings(2)
                .ingredients(List.of())
                .tags(List.of())
                .build();
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(recipeEntity)
                .savedList(savedList)
                .portions(2)
                .build();
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.of(savedList));
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry));
        service.removeRecipeFromList(recipeId, listName);

        Mockito.verify(entryRepository).delete(entry);
    }

    @Test
        void throwExceptionWhenListNameIsBlank() {
            Exception exception = assertThrows(ListValidationException.class, () ->
                    new ListName("")
            );

            assertEquals("List name cannot be null or empty.", exception.getMessage());
        }

    @Test
    void throwExceptionWhenRecipeListDoesNotExist() {
        ListName listName = new ListName("NonExistingList");
        UUID recipeId = UUID.randomUUID();
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ListNotFoundException.class, () ->
                service.removeRecipeFromList(recipeId, listName)
        );
        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
    }

    @Test
    void throwExceptionWhenRecipeIdIsNull() {
        ListName listName = new ListName("ExistingList");

        Exception exception = assertThrows(RecipeValidationException.class, () ->
                service.removeRecipeFromList(null, listName)
        );
        assertEquals("Recipe ID cannot be null.", exception.getMessage());
    }

    @Test
    void throwExceptionWhenRecipeListIsEmpty() {
        ListName listName = new ListName("EmptyList");
        SavedRecipeList savedList = SavedRecipeList.builder()
                .listName(listName.name())
                .build();
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.of(savedList));
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of());

        Exception exception = assertThrows(RecipeNotFoundException.class, () ->
                service.removeRecipeFromList(UUID.randomUUID(), listName)
        );
        assertEquals("Recipe not found in list.", exception.getMessage());
    }

    @Test
    void throwExceptionWhenRecipeDoesNotExistInList() {
        ListName listName = new ListName("Desserts");
        UUID existingRecipeId = UUID.randomUUID();
        UUID missingRecipeId = UUID.randomUUID();
        SavedRecipeList savedList = SavedRecipeList.builder().listName(listName.name()).build();
        RecipeEntity recipeEntity = mock(RecipeEntity.class);
        Mockito.when(recipeEntity.getRecipeId()).thenReturn(existingRecipeId);
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(recipeEntity)
                .savedList(savedList)
                .portions(2)
                .build();
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.of(savedList));
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry));

        Exception exception = assertThrows(RecipeNotFoundException.class, () ->
                service.removeRecipeFromList(missingRecipeId, listName)
        );
        assertEquals("Recipe not found in list.", exception.getMessage());
    }

    @Test
    void clearRecipeList() {
        ListName listName = new ListName("Desserts");
        SavedRecipeList savedList = SavedRecipeList.builder().listName(listName.name()).build();
        RecipeEntity recipeEntity = mock(RecipeEntity.class);
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(recipeEntity)
                .savedList(savedList)
                .portions(2)
                .build();
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry));
        boolean result = service.clearRecipeList(listName, true);

        assertTrue(result);
        Mockito.verify(entryRepository).deleteAll(List.of(entry));
    }

    @Test
    void notClearRecipeListWhenConfirmationIsFalse() {
        ListName listName = new ListName("Desserts");
        boolean result = service.clearRecipeList(listName, false);

        assertFalse(result);
        Mockito.verify(entryRepository, Mockito.never()).deleteAll(Mockito.any());
    }

    @Test
    void throwExceptionWhenListNameIsBlankDuringClear() {
        Exception exception = assertThrows(ListValidationException.class, () ->
                new ListName("")
        );
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void throwExceptionWhenClearingNonExistingList() {
        ListName listName = new ListName("NonExistingList");
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of());

        Exception exception = assertThrows(ListNotFoundException.class, () ->
                service.clearRecipeList(listName, true)
        );
        assertEquals("List is empty or not found.", exception.getMessage());
    }

    @Test
    void deleteRecipeListSuccessfully() {
        ListName listName = new ListName("Desserts");
        SavedRecipeList savedList = SavedRecipeList.builder()
                .listName(listName.name())
                .build();
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.of(savedList));
        service.deleteRecipeList(listName);

        Mockito.verify(savedListRepository).delete(savedList);
    }

    @Test
    void throwExceptionWhenDeletingListWithBlankName() {
        Exception exception = assertThrows(ListValidationException.class, () ->
                new ListName("")
        );
        assertEquals("List name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingList() {
        ListName listName = new ListName("NonExistingList");
        Mockito.when(savedListRepository.findByListName(listName.name()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ListNotFoundException.class, () ->
                service.deleteRecipeList(listName)
        );
        assertEquals("List not found.", exception.getMessage());
    }

    @Test
    void generateShoppingListWithScaledIngredients() {
        ListName listName = new ListName("Dinner");
        UUID productId = UUID.randomUUID();
        IngredientJson ingJson = new IngredientJson(productId, "Flour", 200F, Unit.G);

        RecipeEntity recipeEntity = RecipeEntity.newRecipeEntity(
                UUID.randomUUID(), "Pancakes", Category.MAIN_COURSE,
                List.of(ingJson), "Fry", 2, List.of()
        );
        Product product = Product.newProduct(productId, new ProductName("Flour"));
        Ingredient ing = Ingredient.newIngredient(product, 200F, Unit.G);
        Recipe recipe = Recipe.newRecipe(
                recipeEntity.getRecipeId(),
                recipeEntity.getRecipeName(),
                recipeEntity.getCategory(),
                List.of(ing),
                recipeEntity.getInstructions(),
                recipeEntity.getNumberOfServings(),
                List.of()
        );
        Mockito.when(recipeMapper.toDomain(recipeEntity)).thenReturn(recipe);
        RecipeListEntry entry = RecipeListEntry.builder()
                .recipe(recipeEntity)
                .savedList(SavedRecipeList.builder().listName(listName.name()).build())
                .portions(4)
                .build();
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry));
        Map<String, Float> result = service.generateShoppingList(listName);

        assertEquals(1, result.size());
        assertEquals(400F, result.get("Flour"));
    }

    @Test
    void shouldMergeIngredientsAcrossMultipleRecipes() {
        ListName listName = new ListName("Desserts");
        Ingredient butter100 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Butter")), 100, Unit.G);
        Ingredient butter250 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Butter")), 250, Unit.G);
        Ingredient sugar250 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Sugar")), 250, Unit.G);
        List<Ingredient> ingredients = List.of(butter100, butter250, sugar250);
        RecipeEntity recipeEntity1 = mock(RecipeEntity.class);
        RecipeEntity recipeEntity2 = mock(RecipeEntity.class);
        Recipe recipe1 = mock(Recipe.class);
        Recipe recipe2 = mock(Recipe.class);
        SavedRecipeList savedList = SavedRecipeList.builder().listName(listName.name()).build();
        Mockito.when(recipeMapper.toDomain(recipeEntity1)).thenReturn(recipe1);
        Mockito.when(recipeMapper.toDomain(recipeEntity2)).thenReturn(recipe2);
        Mockito.when(recipe1.getIngredients()).thenReturn(ingredients);
        Mockito.when(recipe2.getIngredients()).thenReturn(ingredients);
        Mockito.when(recipe1.getNumberOfServings()).thenReturn(2);
        Mockito.when(recipe2.getNumberOfServings()).thenReturn(2);
        RecipeListEntry entry1 = RecipeListEntry.builder()
                .recipe(recipeEntity1)
                .savedList(savedList)
                .portions(2)
                .build();
        RecipeListEntry entry2 = RecipeListEntry.builder()
                .recipe(recipeEntity2)
                .savedList(savedList)
                .portions(2)
                .build();
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of(entry1, entry2));
        Map<String, Float> shoppingList = service.generateShoppingList(listName);

        assertEquals(700F, shoppingList.get("Butter"));
        assertEquals(500F, shoppingList.get("Sugar"));
    }

    @Test
    void shouldReturnEmptyShoppingListForNonExistingList() {
        ListName listName = new ListName("NonExistingList");
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of());
        Map<String, Float> shoppingList = service.generateShoppingList(listName);

        assertNotNull(shoppingList);
        assertTrue(shoppingList.isEmpty());
    }

    @Test
    void shouldReturnEmptyShoppingListWhenRecipeListIsEmpty() {
        ListName listName = new ListName("Desserts");
        Mockito.when(entryRepository.findBySavedList_ListName(listName.name()))
                .thenReturn(List.of());
        Map<String, Float> shoppingList = service.generateShoppingList(listName);
        assertNotNull(shoppingList);
        assertTrue(shoppingList.isEmpty());

    }
}

