package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryValuesExistence() {
        Category[] categories = Category.values();
        assertEquals(12, categories.length);
    }

    @Test
    void shouldReturnCorrectEnumValueByName() {
        assertThat(Category.valueOf("APPETIZER")).isEqualTo(Category.APPETIZER);
        assertThat(Category.valueOf("SOUP")).isEqualTo(Category.SOUP);
        assertThat(Category.valueOf("MAIN_DISH")).isEqualTo(Category.MAIN_DISH);
    }

}




