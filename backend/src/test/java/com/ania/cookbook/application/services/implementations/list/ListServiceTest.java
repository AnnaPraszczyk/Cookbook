//package com.ania.cookbook.application.services.implementations.list;
//
//import com.ania.cookbook.application.services.implementations.product.ProductName;
//import com.ania.cookbook.application.services.interfaces.list.ListUseCase.ListName;
//import com.ania.cookbook.domain.exceptions.ListNotFoundException;
//import com.ania.cookbook.domain.exceptions.ListValidationException;
//import com.ania.cookbook.domain.exceptions.RecipeNotFoundException;
//import com.ania.cookbook.domain.exceptions.RecipeValidationException;
//import com.ania.cookbook.domain.model.*;
//import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
//import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
//import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
//import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
//import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
//import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
//import com.ania.cookbook.infrastructure.persistence.list.ListEntryRepository;
//import com.ania.cookbook.infrastructure.persistence.list.SavedListRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.time.Instant;
//import java.util.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ListServiceTest {
//    @Mock
//    private ReadRecipe readRecipeRepository;
//    @Mock
//    private ListEntryRepository entryRepository;
//    @Mock
//    private SavedListRepository savedListRepository;
//    @Mock
//    private RecipeMapper recipeMapper;
//    @InjectMocks
//    private ListService service;
//
//    private ListName listName;
//    private SavedListEntity savedList;
//    private UUID entryId;
//
//    @BeforeEach
//    void setup() {
//        listName = new ListName("TestList");
//        entryId = UUID.randomUUID();
//        savedList = SavedListEntity.builder()
//                .listName(listName.name())
//                .createdAt(Instant.now())
//                .listDescription("")
//                .expectedPortions(0)
//                .entries(new ArrayList<>())
//                .build();
//    }
//    @Test
//    void createNewList() {
//        when(savedListRepository.existsByListName(listName.name())).thenReturn(false);
//        String description = "list description";
//        int defaultPortions = 4;
//        service.createRecipeList(listName, description, defaultPortions);
//
//        verify(savedListRepository).save(Mockito.argThat(list ->
//                list.getListName().equals(savedList.getListName()) &&
//                        list.getListDescription().equals(description) &&
//                        list.getExpectedPortions() == defaultPortions &&
//                        list.getEntries().isEmpty()
//        ));
//    }
//
//    @Test
//    void createRecipeListWhenNameIsBlank() {
//        assertThrows(ListValidationException.class,
//                () -> service.createRecipeList(new ListName(""),"List description", 4));
//    }
//
//    @Test
//    void createRecipeListWhenNameIsNull() {
//        assertThrows(ListValidationException.class,
//                () -> service.createRecipeList(new ListName(null),"List description", 2));
//    }
//
//    @Test
//    void shouldAddNewRecipeEntryWhenEntryNotExists() {
//        UUID recipeId = UUID.randomUUID();
//        int portions = 6;
//        Recipe recipe = Recipe.newRecipe(
//                recipeId,
//                "Pizza",
//                Category.MAIN_COURSE,
//                List.of(),
//                "Bake.",
//                6,
//                List.of("new")
//        );
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(recipeId)
//                .recipeName("Pizza")
//                .category(Category.MAIN_COURSE)
//                .ingredients(List.of())
//                .instructions("Bake.")
//                .numberOfServings(2)
//                .tags(List.of("new"))
//                .build();
//
//        when(entryRepository.findByRecipe_RecipeIdAndSavedList_ListName(recipeId, listName.name()))
//                .thenReturn(Optional.empty());
//        when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
//        when(savedListRepository.findByListName(listName.name())).thenReturn(Optional.of(savedList));
//        when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
//
//        ListEntryEntity toSave = ListEntryEntity.builder()
//                .recipe(recipeEntity).savedList(savedList).portions(portions).build();
//        when(entryRepository.save(any())).thenReturn(toSave);
//
//        ListEntryEntity result = service.addRecipeToList(recipeId, listName, portions);
//
//        assertEquals(recipeEntity, result.getRecipe());
//        assertEquals(savedList, result.getSavedList());
//        assertEquals(portions, result.getPortions());
//    }
//
//    @Test
//    void shouldUpdateEntryWhenPortionsDifferent() {
//        int portions = 6;
//        ListEntryEntity entry = ListEntryEntity.builder().portions(2).build();
//
//        ListEntryEntity result = service.updateRecipeEntry(entry, portions);
//
//        verify(entryRepository).save(entry);
//        assertEquals(portions, result.getPortions());
//    }
//
//    @Test
//    void shouldNotUpdateEntryWhenPortionsSame() {
//        int portions = 6;
//        ListEntryEntity entry = ListEntryEntity.builder().portions(portions).build();
//
//        ListEntryEntity result = service.updateRecipeEntry(entry, portions);
//
//        verify(entryRepository, never()).save(any());
//        assertEquals(portions, result.getPortions());
//    }
//
//    @Test
//    void addRecipeByIdWhenRecipeNotFound() {
//        UUID fakeRecipeId = UUID.randomUUID();
//        int customPortions = 6;
//        ListName listName = new ListName("New List");
//        when(readRecipeRepository.findRecipeById(fakeRecipeId))
//                .thenReturn(Optional.empty());
//
//        assertThrows(RecipeNotFoundException.class, () ->
//                service.addRecipeToList(fakeRecipeId, listName, customPortions));
//    }
//
//    @Test
//    void shouldNotCreateDuplicateRecipeEntryWhenAlreadyExistsInList() {
//        ListName listName = new ListName("Desserts");
//        int customPortions = 2;
//        UUID recipeId = UUID.randomUUID();
//        IngredientJson ingredient = new IngredientJson(UUID.randomUUID(), "Flour", 200F, Unit.G);
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(recipeId)
//                .recipeName("Test")
//                .category(Category.DESSERT)
//                .ingredients(List.of(ingredient))
//                .instructions("Mix")
//                .numberOfServings(2)
//                .tags(List.of())
//                .build();
//        Recipe recipe = mock(Recipe.class);
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(customPortions)
//                .build();
//        when(entryRepository.findByRecipe_RecipeIdAndSavedList_ListName(recipeId, listName.name()))
//                .thenReturn(Optional.empty())
//                .thenReturn(Optional.of(entry));
//        when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
//        when(savedListRepository.findByListName(listName.name())).thenReturn(Optional.of(savedList));
//        when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
//        when(entryRepository.save(any())).thenReturn(entry);
//        service.addRecipeToList(recipeId, listName, customPortions);
//
//        service.addRecipeToList(recipeId, listName, customPortions);
//
//        verify(entryRepository, times(1)).save(argThat(e ->
//                e.getRecipe().getRecipeId().equals(recipeId) &&
//                        e.getPortions() == customPortions
//        ));
//    }
//
//    @Test
//    void shouldUpdateRecipeEntryWhenAlreadyExistsWithDifferentPortions() {
//        ListName listName = new ListName("Desserts");
//        int originalPortions = 2;
//        int newPortions = 6;
//        UUID recipeId = UUID.randomUUID();
//        IngredientJson ingredient = new IngredientJson(UUID.randomUUID(), "Flour", 200F, Unit.G);
//
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(recipeId)
//                .recipeName("Test")
//                .category(Category.DESSERT)
//                .ingredients(List.of(ingredient))
//                .instructions("Mix")
//                .numberOfServings(originalPortions)
//                .tags(List.of())
//                .build();
//
//        Recipe recipe = mock(Recipe.class);
//        ListEntryEntity existingEntry = ListEntryEntity.builder()
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(originalPortions)
//                .build();
//
//        ListEntryEntity updatedEntry = ListEntryEntity.builder()
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(newPortions)
//                .build();
//        when(entryRepository.findByRecipe_RecipeIdAndSavedList_ListName(recipeId, listName.name()))
//                .thenReturn(Optional.empty())
//                .thenReturn(Optional.of(existingEntry));
//
//        when(readRecipeRepository.findRecipeById(recipeId)).thenReturn(Optional.of(recipe));
//        when(savedListRepository.findByListName(listName.name())).thenReturn(Optional.of(savedList));
//        when(recipeMapper.toEntity(recipe)).thenReturn(recipeEntity);
//        when(entryRepository.save(any())).thenReturn(updatedEntry);
//
//        service.addRecipeToList(recipeId, listName, originalPortions);
//
//        service.addRecipeToList(recipeId, listName, newPortions);
//
//        verify(entryRepository).save(argThat(e ->
//                e.getPortions() == originalPortions &&
//                        e.getRecipe().getRecipeId().equals(recipeId)
//        ));
//
//        verify(entryRepository).save(argThat(e ->
//                e.getPortions() == newPortions &&
//                        e.getRecipe().getRecipeId().equals(recipeId)
//        ));
//    }
//
//    @Test
//    void returnRecipesFromList() {
//        ListName listName = new ListName("Desserts");
//        UUID recipeId = UUID.randomUUID();
//        when(savedListRepository.existsById(listName.name())).thenReturn(true);
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(recipeId)
//                .recipeName("Test")
//                .category(Category.DESSERT)
//                .instructions("Mix")
//                .numberOfServings(2)
//                .ingredients(List.of())
//                .tags(List.of())
//                .build();
//        Recipe recipe = mock(Recipe.class);
//        when(recipeMapper.toDomain(recipeEntity)).thenReturn(recipe);
//        when(recipe.getRecipeName()).thenReturn("Pancakes");
//        SavedListEntity savedList = SavedListEntity.builder()
//                .listName(listName.name())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(2)
//                .build();
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of(entry));
//        List<Recipe> result = service.getRecipesList(listName);
//
//        assertEquals(1, result.size());
//        assertEquals("Pancakes", result.getFirst().getRecipeName());
//    }
//
//    @Test
//    void throwsExceptionWhenRecipeListDoesNotExist() {
//        ListName listName = new ListName("NonExistingList");
//        when(savedListRepository.existsById(listName.name())).thenReturn(false);
//
//        assertThrows(ListNotFoundException.class, () -> service.getRecipesList(listName));
//    }
//
//    @Test
//    void returnsEmptyListWhenNoEntriesFoundInExistingList() {
//        ListName listName = new ListName("EmptyList");
//        when(savedListRepository.existsById(listName.name())).thenReturn(true);
//        when(entryRepository.findBySavedList_ListName(listName.name())).thenReturn(List.of());
//        List<Recipe> result = service.getRecipesList(listName);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void removeRecipeFromList() {
//        ListName listName = new ListName("Desserts");
//        SavedListEntity savedList = SavedListEntity.builder().listName(listName.name()).build();
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Test")
//                .category(Category.DESSERT)
//                .instructions("Mix")
//                .numberOfServings(2)
//                .ingredients(List.of())
//                .tags(List.of())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(2)
//                .build();
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.of(savedList));
//        when(entryRepository.findById(entryId))
//                .thenReturn(Optional.of(entry));
//        service.removeRecipeFromList(entryId, listName);
//
//        verify(entryRepository).delete(entry);
//    }
//
//    @Test
//        void throwExceptionWhenListNameIsBlank() {
//            Exception exception = assertThrows(ListValidationException.class, () ->
//                    new ListName("")
//            );
//
//            assertEquals("List name cannot be null or empty.", exception.getMessage());
//        }
//
//    @Test
//    void throwExceptionWhenRecipeListDoesNotExist() {
//        ListName listName = new ListName("NonExistingList");
//        UUID recipeId = UUID.randomUUID();
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ListNotFoundException.class, () ->
//                service.removeRecipeFromList(recipeId, listName)
//        );
//        assertEquals("Recipe list with the given name does not exist.", exception.getMessage());
//    }
//
//    @Test
//    void throwExceptionWhenRecipeIdIsNull() {
//        ListName listName = new ListName("ExistingList");
//
//        Exception exception = assertThrows(RecipeValidationException.class, () ->
//                service.removeRecipeFromList(null, listName)
//        );
//        assertEquals("Recipe ID cannot be null.", exception.getMessage());
//    }
//
//    @Test
//    void throwExceptionWhenRecipeListIsEmpty() {
//        ListName listName = new ListName("EmptyList");
//        SavedListEntity savedList = SavedListEntity.builder()
//                .listName(listName.name())
//                .build();
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.of(savedList));
//        when(entryRepository.findById(entryId))
//                .thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(RecipeNotFoundException.class, () ->
//                service.removeRecipeFromList(entryId, listName)
//        );
//        assertEquals("Recipe not found in list.", exception.getMessage());
//    }
//
//    @Test
//    void throwExceptionWhenRecipeDoesNotExistInList() {
//        ListName listName = new ListName("Desserts");
//        ListName otherList = new ListName("OtherList");
//        SavedListEntity savedListInTest = SavedListEntity.builder().listName(listName.name()).build();
//        SavedListEntity savedListInEntry = SavedListEntity.builder().listName(otherList.name()).build();
//        RecipeEntity recipeEntity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .recipe(recipeEntity)
//                .savedList(savedListInEntry)
//                .portions(2)
//                .build();
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.of(savedListInTest));
//        when(entryRepository.findById(entryId))
//                .thenReturn(Optional.of(entry));
//
//        Exception exception = assertThrows(RecipeValidationException.class, () ->
//                service.removeRecipeFromList(entryId, listName)
//        );
//        assertEquals("Recipe entry does not belong to the specified list.", exception.getMessage());
//    }
//
//    @Test
//    void clearRecipeList() {
//        ListName listName = new ListName("Desserts");
//        SavedListEntity savedList = SavedListEntity.builder().listName(listName.name()).build();
//        RecipeEntity recipeEntity = mock(RecipeEntity.class);
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(2)
//                .build();
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of(entry));
//        boolean result = service.clearRecipeList(listName, true);
//
//        assertTrue(result);
//        verify(entryRepository).deleteAll(List.of(entry));
//    }
//
//    @Test
//    void notClearRecipeListWhenConfirmationIsFalse() {
//        ListName listName = new ListName("Desserts");
//        boolean result = service.clearRecipeList(listName, false);
//
//        assertFalse(result);
//        verify(entryRepository, Mockito.never()).deleteAll(any());
//    }
//
//    @Test
//    void throwExceptionWhenListNameIsBlankDuringClear() {
//        Exception exception = assertThrows(ListValidationException.class, () ->
//                new ListName("")
//        );
//        assertEquals("List name cannot be null or empty.", exception.getMessage());
//    }
//
//    @Test
//    void throwExceptionWhenClearingNonExistingList() {
//        ListName listName = new ListName("NonExistingList");
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of());
//
//        Exception exception = assertThrows(ListNotFoundException.class, () ->
//                service.clearRecipeList(listName, true)
//        );
//        assertEquals("List is empty or not found.", exception.getMessage());
//    }
//
//    @Test
//    void deleteRecipeListSuccessfully() {
//        ListName listName = new ListName("Desserts");
//        SavedListEntity savedList = SavedListEntity.builder()
//                .listName(listName.name())
//                .build();
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.of(savedList));
//        service.deleteRecipeList(listName);
//
//        verify(savedListRepository).delete(savedList);
//    }
//
//    @Test
//    void throwExceptionWhenDeletingListWithBlankName() {
//        Exception exception = assertThrows(ListValidationException.class, () ->
//                new ListName("")
//        );
//        assertEquals("List name cannot be null or empty.", exception.getMessage());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenDeletingNonExistingList() {
//        ListName listName = new ListName("NonExistingList");
//        when(savedListRepository.findByListName(listName.name()))
//                .thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ListNotFoundException.class, () ->
//                service.deleteRecipeList(listName)
//        );
//        assertEquals("List not found.", exception.getMessage());
//    }
//
//    @Test
//    void generateShoppingListWithScaledIngredients() {
//        ListName listName = new ListName("Dinner");
//        UUID productId = UUID.randomUUID();
//        IngredientJson ingJson = new IngredientJson(productId, "Flour", 200F, Unit.G);
//
//        RecipeEntity recipeEntity = RecipeEntity.newRecipeEntity(
//                UUID.randomUUID(), "Pancakes", Category.MAIN_COURSE,
//                List.of(ingJson), "Fry", 2, List.of()
//        );
//        Product product = Product.newProduct(productId, new ProductName("Flour"));
//        Ingredient ing = Ingredient.newIngredient(product, 200F, Unit.G);
//        Recipe recipe = Recipe.newRecipe(
//                recipeEntity.getRecipeId(),
//                recipeEntity.getRecipeName(),
//                recipeEntity.getCategory(),
//                List.of(ing),
//                recipeEntity.getInstructions(),
//                recipeEntity.getNumberOfServings(),
//                List.of()
//        );
//        when(savedListRepository.existsByListName(listName.name()))
//                .thenReturn(true);
//        when(recipeMapper.toDomain(recipeEntity)).thenReturn(recipe);
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .recipe(recipeEntity)
//                .savedList(savedList)
//                .portions(4)
//                .build();
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of(entry));
//        Map<String, Float> result = service.generateShoppingList(listName);
//
//        assertEquals(1, result.size());
//        assertEquals(400F, result.get("Flour"));
//    }
//
//    @Test
//    void shouldMergeIngredientsAcrossMultipleRecipes() {
//        ListName listName = new ListName("Desserts");
//        Ingredient butter100 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Butter")), 100, Unit.G);
//        Ingredient butter250 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Butter")), 250, Unit.G);
//        Ingredient sugar250 = Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(),new ProductName("Sugar")), 250, Unit.G);
//        List<Ingredient> ingredients = List.of(butter100, butter250, sugar250);
//        RecipeEntity recipeEntity1 = mock(RecipeEntity.class);
//        RecipeEntity recipeEntity2 = mock(RecipeEntity.class);
//        Recipe recipe1 = mock(Recipe.class);
//        Recipe recipe2 = mock(Recipe.class);
//        SavedListEntity savedList = SavedListEntity.builder().listName(listName.name()).build();
//        when(recipeMapper.toDomain(recipeEntity1)).thenReturn(recipe1);
//        when(recipeMapper.toDomain(recipeEntity2)).thenReturn(recipe2);
//        when(recipe1.getIngredients()).thenReturn(ingredients);
//        when(recipe2.getIngredients()).thenReturn(ingredients);
//        when(recipe1.getNumberOfServings()).thenReturn(2);
//        when(recipe2.getNumberOfServings()).thenReturn(2);
//        ListEntryEntity entry1 = ListEntryEntity.builder()
//                .recipe(recipeEntity1)
//                .savedList(savedList)
//                .portions(2)
//                .build();
//        ListEntryEntity entry2 = ListEntryEntity.builder()
//                .recipe(recipeEntity2)
//                .savedList(savedList)
//                .portions(2)
//                .build();
//        when(savedListRepository.existsByListName(listName.name()))
//                .thenReturn(true);
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of(entry1, entry2));
//        Map<String, Float> shoppingList = service.generateShoppingList(listName);
//
//        assertEquals(700F, shoppingList.get("Butter"));
//        assertEquals(500F, shoppingList.get("Sugar"));
//    }
//
//    @Test
//    void shouldReturnEmptyShoppingListForNonExistingList() {
//        ListName listName = new ListName("NonExistingList");
//        when(savedListRepository.existsByListName(listName.name()))
//                .thenReturn(true);
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of());
//        Map<String, Float> shoppingList = service.generateShoppingList(listName);
//
//        assertNotNull(shoppingList);
//        assertTrue(shoppingList.isEmpty());
//    }
//
//    @Test
//    void shouldReturnEmptyShoppingListWhenRecipeListIsEmpty() {
//        ListName listName = new ListName("Desserts");
//        when(savedListRepository.existsByListName(listName.name()))
//                .thenReturn(true);
//        when(entryRepository.findBySavedList_ListName(listName.name()))
//                .thenReturn(List.of());
//        Map<String, Float> shoppingList = service.generateShoppingList(listName);
//
//        assertNotNull(shoppingList);
//        assertTrue(shoppingList.isEmpty());
//    }
//}
//
