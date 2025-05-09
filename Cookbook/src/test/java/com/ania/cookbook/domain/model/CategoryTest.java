package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
        void testGetDisplayName() {
        assertEquals("Appetizer", Category.APPETIZER.getDisplayName());
        assertEquals("Soup", Category.SOUP.getDisplayName());
        assertEquals("Main Dish", Category.MAIN_DISH.getDisplayName());
        assertEquals("Sauce", Category.SAUCE.getDisplayName());
        assertEquals("Salad", Category.SALAD.getDisplayName());
        assertEquals("Pasta", Category.PASTA.getDisplayName());
        assertEquals("Snack", Category.SNACK.getDisplayName());
        assertEquals("Other", Category.OTHER.getDisplayName());
        assertEquals("Dessert", Category.DESSERT.getDisplayName());
        assertEquals("Cake", Category.CAKE.getDisplayName());
        assertEquals("Pie", Category.PIE.getDisplayName());
        assertEquals("Bakery", Category.BAKERY.getDisplayName());
       }

    @Test
    void testCategoryValuesExistence() {
        Category[] categories = Category.values();
        assertEquals(12, categories.length);
    }
}


