//package com.ania.cookbook.infrastructure.mapper;
//
//import com.ania.cookbook.application.services.implementations.product.ProductName;
//import com.ania.cookbook.application.services.interfaces.list.ListUseCase.ListName;
//import com.ania.cookbook.domain.exceptions.ListValidationException;
//import com.ania.cookbook.domain.exceptions.RecipeValidationException;
//import com.ania.cookbook.domain.model.*;
//import com.ania.cookbook.infrastructure.persistence.entity.IngredientJson;
//import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
//import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
//import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
//import com.ania.cookbook.web.recipe.ReadRecipeResponse;
//import com.ania.cookbook.web.list.ListEntryResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.util.List;
//import java.util.UUID;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class ListEntryEntityMapperTest {
//    private RecipeMapper recipeMapper;
//    private ListEntryMapper entryMapper;
//
//    @BeforeEach
//    void setUp() {
//        recipeMapper = mock(RecipeMapper.class);
//        entryMapper = new ListEntryMapper(recipeMapper);
//    }
//
//    @Test
//    void mapEntityToResponseCorrectly() {
//        UUID entryId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        String productName = "Flour";
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of(new IngredientJson(productId, productName, 500f, Unit.G)))
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of("easy"))
//                .build();
//        Recipe recipe = Recipe.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Flour")), 500f, Unit.G)))
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of("easy"))
//                .build();
//        ReadRecipeResponse response = ReadRecipeResponse.from(recipe);
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .portions(2)
//                .recipe(entity)
//                .build();
//        when(recipeMapper.toDomain(entity)).thenReturn(recipe);
//        ListEntryResponse result = entryMapper.toResponse(entry);
//
//        assertEquals(entryId, result.getEntryId());
//        assertEquals(2, result.getPortions());
//        assertEquals(response, result.getRecipe());
//        assertEquals(response.getId(), result.getRecipe().getId());
//        assertEquals(response.getName(), result.getRecipe().getName());
//        assertEquals(response.getCategory(), result.getRecipe().getCategory());
//        assertEquals(response.getIngredients(), result.getRecipe().getIngredients());
//        assertEquals(response.getInstructions(), result.getRecipe().getInstructions());
//        assertEquals(response.getNumberOfServings(), result.getRecipe().getNumberOfServings());
//        assertEquals(response.getTags(), result.getRecipe().getTags());
//    }
//
//    @Test
//    void throwExceptionWhenRecipeIsNull() {
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(null)
//                .build();
//
//        assertThrows(NullPointerException.class, () -> entryMapper.toResponse(entry));
//    }
//
//    @Test
//    void throwExceptionWhenRecipeMapperReturnsNull() {
//        UUID entryId = UUID.randomUUID();
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .portions(2)
//                .recipe(entity)
//                .build();
//        when(recipeMapper.toDomain(entity)).thenReturn(null);
//
//        assertThrows(NullPointerException.class, () -> entryMapper.toResponse(entry));
//    }
//
//    @Test
//    void throwExceptionWhenPortionsIsZero() {
//        UUID entryId = UUID.randomUUID();
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of())
//                .build();
//
//        Recipe recipe = Recipe.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of())
//                .build();
//        when(recipeMapper.toDomain(entity)).thenReturn(recipe);
//
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(entryId)
//                .portions(0)
//                .recipe(entity)
//                .build();
//
//        ListEntryResponse result = entryMapper.toResponse(entry);
//        assertEquals(0, result.getPortions());
//    }
//
////     trzeba dodać mapowanie listy w serwisie
//
//    @Test
//    void throwExceptionWhenRecipeIsNullToDomain() {
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(null)
//                .savedList(new SavedListEntity())
//                .build();
//
//        assertThrows(RecipeValidationException.class, () -> entryMapper.toDomain(entry));
//    }
//
//    @Test
//    void throwExceptionWhenSavedListIsNullToDomain() {
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Test")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Cook")
//                .numberOfServings(1)
//                .tags(List.of())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(entity)
//                .savedList(null)
//                .build();
//
//        assertThrows(RecipeValidationException.class, () -> entryMapper.toDomain(entry));
//    }
//
//    @Test
//    void throwExceptionWhenRecipeMapperReturnsNullToDomain() {
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Test")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Cook")
//                .numberOfServings(1)
//                .tags(List.of())
//                .build();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(entity)
//                .savedList(new SavedListEntity())
//                .build();
//        when(recipeMapper.toDomain(entity)).thenReturn(null);
//
//        assertThrows(RecipeValidationException.class, () -> entryMapper.toDomain(entry));
//    }
//
//    @Test
//    void throwExceptionWhenPortionsIsNegativeToDomain() {
//        RecipeEntity entity = RecipeEntity.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Test")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Cook")
//                .numberOfServings(1)
//                .tags(List.of())
//                .build();
//        SavedListEntity savedListEntity = new SavedListEntity();
//        ListEntryEntity entry = ListEntryEntity.builder()
//                .entryId(UUID.randomUUID())
//                .portions(-3)
//                .recipe(entity)
//                .savedList(savedListEntity)
//                .build();
//
//        assertThrows(RecipeValidationException.class, () -> entryMapper.toDomain(entry));
//    }
//
////    @Test
////    void mapDomainToEntityCorrectly() {
////        UUID entryId = UUID.randomUUID();
////        UUID productId = UUID.randomUUID();
////        ListName listName = new ListName("My list");
////        String productName = "Flour";
////        RecipeEntity entity = RecipeEntity.builder()
////                .recipeId(UUID.randomUUID())
////                .recipeName("Soup")
////                .category(Category.SOUP)
////                .ingredients(List.of(new IngredientJson(productId, productName, 500f, Unit.G)))
////                .instructions("Bake")
////                .numberOfServings(2)
////                .tags(List.of("easy"))
////                .build();
////        Recipe recipe = Recipe.builder()
////                .recipeId(UUID.randomUUID())
////                .recipeName("Soup")
////                .category(Category.SOUP)
////                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Flour")), 500f, Unit.G)))
////                .instructions("Bake")
////                .numberOfServings(2)
////                .tags(List.of("easy"))
////                .build();
////        SavedRecipeListDomain savedListDomain = SavedRecipeListDomain.builder()
////                .listName(listName)
////                .expectedPortions(2)
////                .build();
////        SavedRecipeList savedListEntity = new SavedRecipeList();
////        RecipeListEntryDomain domain = RecipeListEntryDomain.builder()
////                .entryId(entryId)
////                .portions(4)
////                .recipe(recipe)
////                .savedRecipeList(savedListDomain)
////                .build();
////        when(recipeMapper.toEntity(recipe)).thenReturn(entity);
////        RecipeListEntry partialEntry = RecipeListEntry.builder()
////                .entryId(entryId)
////                .recipe(entity)
////                .portions(4)
////                .build();
////        when(savedListMapper.toEntity(eq(savedListDomain), anyList()))
////                .thenReturn(savedListEntity);
////        RecipeListEntry result = entryMapper.toEntity(domain);
////
////        assertEquals(entryId, result.getEntryId());
////        assertEquals(4, result.getPortions());
////        assertEquals(entity, result.getRecipe());
////        assertEquals(savedListEntity, result.getSavedList());
////    }
//
//    @Test
//    void throwExceptionWhenRecipeIsNullToEntity() {
//        SavedList savedListDomain = SavedList.builder()
//                .listName(new ListName("List"))
//                .expectedPortions(2)
//                .build();
//
//        assertThrows(RecipeValidationException.class, () -> ListEntry.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(null)
//                .savedRecipeList(savedListDomain)
//                .build());
//    }
//
//    @Test
//    void throwExceptionWhenSavedRecipeListIsNullToEntity() {
//        Recipe recipe = Recipe.builder()
//                .recipeId(UUID.randomUUID())
//                .recipeName("Soup")
//                .category(Category.SOUP)
//                .ingredients(List.of())
//                .instructions("Bake")
//                .numberOfServings(2)
//                .tags(List.of())
//                .build();
//
//        assertThrows(ListValidationException.class, () -> ListEntry.builder()
//                .entryId(UUID.randomUUID())
//                .portions(2)
//                .recipe(recipe)
//                .savedRecipeList(null)
//                .build());
//    }
//}