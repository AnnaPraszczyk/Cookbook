package com.ania.cookbook.application.services.implementations.recipe;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRecipeTest {
    private final List<IngredientRequest> validIngredients = List.of(
            new IngredientRequest("Flour", 200, Unit.G));
    private final List<String> validTags = List.of("Easy", "Quick");

        @Test
        void createRecipeWithValidData() {
            CreateRecipe recipe = CreateRecipe.builder()
                    .recipeName("Pancakes")
                    .category(Category.DESSERT)
                    .ingredients(validIngredients)
                    .instructions("Mix and fry.")
                    .numberOfServings(4)
                    .tags(validTags)
                    .build();
            assertEquals("Pancakes", recipe.getRecipeName());
            assertEquals(Category.DESSERT, recipe.getCategory());
            assertEquals(1, recipe.getIngredients().size());
            assertEquals("Mix and fry.", recipe.getInstructions());
            assertEquals(4, recipe.getNumberOfServings());
            assertEquals(validTags, recipe.getTags());
        }

    @Test
    void throwExceptionWhenRecipeNameIsBlank() {
        Exception ex = assertThrows(RecipeValidationException.class, () ->
                CreateRecipe.builder()
                        .recipeName("  ")
                        .category(Category.DESSERT)
                        .ingredients(validIngredients)
                        .instructions("Cook it.")
                        .numberOfServings(2)
                        .tags(validTags)
                        .build()
        );
        assertEquals("Recipe name cannot be null or empty.", ex.getMessage());
    }

    @Test
    void throwExceptionWhenCategoryIsNull() {
        Exception ex = assertThrows(RecipeValidationException.class, () ->
                CreateRecipe.builder()
                        .recipeName("Soup")
                        .category(null)
                        .ingredients(validIngredients)
                        .instructions("Boil it.")
                        .numberOfServings(3)
                        .tags(validTags)
                        .build()
        );
        assertEquals("Recipe category cannot be null.", ex.getMessage());
    }

    @Test
    void throwExceptionWhenInstructionsAreBlank() {
        Exception ex = assertThrows(RecipeValidationException.class, () ->
                CreateRecipe.builder()
                        .recipeName("Salad")
                        .category(Category.SALAD)
                        .ingredients(validIngredients)
                        .instructions(" ")
                        .numberOfServings(1)
                        .tags(validTags)
                        .build()
        );
        assertEquals("Recipe instructions cannot be null or empty.", ex.getMessage());
    }

    @Test
    void throwExceptionWhenNumberOfServingsIsNegative() {
        Exception ex = assertThrows(RecipeValidationException.class, () ->
                CreateRecipe.builder()
                        .recipeName("Cake")
                        .category(Category.DESSERT)
                        .ingredients(validIngredients)
                        .instructions("Bake it.")
                        .numberOfServings(-1)
                        .tags(validTags)
                        .build()
        );
        assertEquals("Recipe number of servings cannot be negative.", ex.getMessage());
    }

    @Test
    void createRecipeWithNullIngredientsList() {
        CreateRecipe recipe = CreateRecipe.builder()
                .recipeName("Toast")
                .category(Category.SNACK)
                .ingredients(null)
                .instructions("Toast the bread.")
                .numberOfServings(1)
                .tags(validTags)
                .build();

        assertNotNull(recipe.getIngredients());
        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void convertToDomainRecipeWithNewProduct() {
        ProductUseCase productUseCase = mock(ProductUseCase.class);
        ProductName productName = ProductName.from("Flour");
        Product newProduct = Product.newProduct(UUID.randomUUID(), productName);
        when(productUseCase.findProductByName(productName)).thenReturn(Optional.empty());
        when(productUseCase.addProduct(productName)).thenReturn(newProduct);
        CreateRecipe createRecipe = CreateRecipe.builder()
                .recipeName("Pancakes")
                .category(Category.DESSERT)
                .ingredients(List.of(new IngredientRequest("Flour", 200, Unit.G)))
                .instructions("Mix and fry.")
                .numberOfServings(2)
                .tags(List.of("Easy"))
                .build();
        Recipe domainRecipe = createRecipe.toDomain(productUseCase);

        assertEquals("Pancakes", domainRecipe.getRecipeName());
        assertEquals(Category.DESSERT, domainRecipe.getCategory());
        assertEquals("Mix and fry.", domainRecipe.getInstructions());
        assertEquals(2, domainRecipe.getNumberOfServings());
        assertEquals(List.of("Easy"), domainRecipe.getTags());
        List<Ingredient> ingredients = domainRecipe.getIngredients();
        assertEquals(1, ingredients.size());
        Ingredient ingredient = ingredients.getFirst();
        assertEquals(newProduct, ingredient.getProduct());
        assertEquals(200, ingredient.getAmount());
        assertEquals(Unit.G, ingredient.getUnit());
        verify(productUseCase).findProductByName(productName);
        verify(productUseCase).addProduct(productName);
    }

    @Test
    void useExistingProductIfFound() {
        ProductUseCase productUseCase = mock(ProductUseCase.class);
        ProductName productName = ProductName.from("Sugar");
        Product existingProduct = Product.newProduct(UUID.randomUUID(), productName);
        when(productUseCase.findProductByName(productName)).thenReturn(Optional.of(existingProduct));
        CreateRecipe createRecipe = CreateRecipe.builder()
                .recipeName("Sweet Toast")
                .category(Category.DESSERT)
                .ingredients(List.of(new IngredientRequest("Sugar", 50, Unit.G)))
                .instructions("Sprinkle sugar on toast.")
                .numberOfServings(1)
                .tags(List.of("Sweet"))
                .build();
        Recipe domainRecipe = createRecipe.toDomain(productUseCase);
        Ingredient ingredient = domainRecipe.getIngredients().getFirst();

        assertEquals(existingProduct, ingredient.getProduct());
        verify(productUseCase).findProductByName(productName);
        verify(productUseCase, never()).addProduct(any());
    }
}
