package com.ania.cookbook.domain.model;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SavedRecipeListDomainTest {
    @Test
    void createObjectWithValidData() {
        SavedRecipeListDomain savedList = SavedRecipeListDomain.builder()
                .listName(new ListName("Dinner"))
                .listDescription("Dinner description")
                .expectedPortions(4)
                .entries(List.of())
                .build();
        Recipe recipe1 = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Baked potatoes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Potatoes")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(2)
                .tags(List.of("fast", "easy"))
                .build();
        Recipe recipe2 = Recipe.builder()
                .recipeId(UUID.randomUUID())
                .recipeName("Pancakes")
                .category(Category.MAIN_COURSE)
                .ingredients(List.of(Ingredient.newIngredient(Product.newProduct(UUID.randomUUID(), new ProductName("Flour")), 500, Unit.G)))
                .instructions("Bake")
                .numberOfServings(4)
                .tags(List.of("fast", "easy"))
                .build();
        RecipeListEntryDomain entry1 = RecipeListEntryDomain.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe1)
                .savedRecipeList(savedList)
                .portions(2)
                .build();
        RecipeListEntryDomain entry2 = RecipeListEntryDomain.builder()
                .entryId(UUID.randomUUID())
                .recipe(recipe2)
                .savedRecipeList(savedList)
                .portions(3)
                .build();

        assertNotNull(entry1);
        assertEquals("Baked potatoes", entry1.getRecipe().getRecipeName());
        assertEquals(2, entry1.getPortions());
        assertNotNull(entry2);
        assertEquals("Pancakes", entry2.getRecipe().getRecipeName());
        assertEquals(3, entry2.getPortions());
        assertEquals(savedList, entry1.getSavedRecipeList());
        assertEquals(savedList, entry2.getSavedRecipeList());
    }

    @Test
    void throwExceptionWhenExpectedPortionsIsZero() {
        assertThrows(ListValidationException.class, () -> {
            SavedRecipeListDomain.builder()
                    .listName(new ListName("Dinner"))
                    .listDescription("Dinner description")
                    .expectedPortions(0)
                    .build();
        });
    }

    @Test
    void setCreatedAtAutomaticallyIfNull() {
        SavedRecipeListDomain domain = SavedRecipeListDomain.builder()
                .listName(new ListName("Dinner"))
                .listDescription("Dinner description")
                .expectedPortions(2)
                .build();

        assertNotNull(domain.getCreatedAt());
    }

    @Test
    void shouldCreateEmptyEntriesListWhenNotProvided() {
        SavedRecipeListDomain domain = SavedRecipeListDomain.builder()
                .listName(new ListName("Breakfast"))
                .listDescription("Breakfast description")
                .expectedPortions(1)
                .build();

        assertNotNull(domain.getEntries());
        assertTrue(domain.getEntries().isEmpty());
    }

    @Test
    void allowEmptyDescription() {
        SavedRecipeListDomain domain = SavedRecipeListDomain.builder()
                .listName(new ListName("Dessert"))
                .listDescription(null)
                .expectedPortions(3)
                .build();

        assertNull(domain.getListDescription());
    }
}