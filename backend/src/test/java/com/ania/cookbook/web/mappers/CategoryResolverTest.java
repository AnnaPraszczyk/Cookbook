package com.ania.cookbook.web.mappers;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryResolverTest {
    private CategoryResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new CategoryResolver();
    }

    @Test
    void shouldResolveValidCategory() {
        Category category = resolver.resolve("Main Course");
        assertEquals(Category.MAIN_COURSE, category);
    }

    @Test
    void shouldTrimInputBeforeResolving() {
        Category category = resolver.resolve("  Dessert  ");
        assertEquals(Category.DESSERT, category);
    }
    @Test
    void shouldThrowExceptionForInvalidCategory() {
        String invalid = "UnknownCategory";
        RecipeValidationException exception = assertThrows(RecipeValidationException.class, () ->
                resolver.resolve(invalid)
        );
        assertEquals("Invalid category: " + invalid, exception.getMessage());
    }
}