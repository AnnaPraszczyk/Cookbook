package com.ania.cookbook.domain.model;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ListEntryTest {
    @Test
    void createValidEntry() {
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Baked potatoes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Potatoes")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();
        SavedList list = SavedList.builder()
                .listName(new ListName("My list"))
                .expectedPortions(4)
                .listDescription("My list description")
                .build();
        ListEntry entry = ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe)
                .savedRecipeList(list)
                .portions(3)
                .build();

        assertNotNull(entry);
        assertEquals("Baked potatoes", entry.getRecipe().getRecipeName());
        assertEquals(3, entry.getPortions());
    }

    @Test
    void throwExceptionWhenEntryIdIsNull() {
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Baked potatoes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Potatoes")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();
        SavedList list = SavedList.builder()
                .listName(new ListName("My list"))
                .expectedPortions(4)
                .listDescription("My list description")
                .build();

        assertThrows(ListValidationException.class, () -> ListEntry.builder()
                .entryId(null)
                .recipe(recipe)
                .savedRecipeList(list)
                .portions(1)
                .build());
    }

    @Test
    void throwExceptionWhenRecipeIsNull() {
        SavedList list = SavedList.builder()
                .listName(new ListName("My list"))
                .expectedPortions(4)
                .listDescription("My list description")
                .build();

        assertThrows(RecipeValidationException.class, () -> ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(null)
                .savedRecipeList(list)
                .portions(1)
                .build());
    }

    @Test
    void throwExceptionWhenSavedListIsNull() {
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Baked potatoes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Potatoes")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();

        assertThrows(ListValidationException.class, () -> ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe)
                .savedRecipeList(null)
                .portions(1)
                .build());
    }

    @Test
    void throwExceptionWhenPortionsAreNegative() {
        Recipe recipe = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Baked potatoes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Potatoes")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();
        SavedList list = SavedList.builder()
                .listName(new ListName("Desserts")).listDescription("Dessert description")
                .expectedPortions(4)
                .build();

        assertThrows(RecipeValidationException.class, () -> ListEntry.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe)
                .savedRecipeList(list)
                .portions(-1)
                .build());
    }
}