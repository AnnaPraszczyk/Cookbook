package com.ania.cookbook.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void CategoryValuesExistence() {
        Category[] categories = Category.values();
        assertEquals(12, categories.length);
    }
}




