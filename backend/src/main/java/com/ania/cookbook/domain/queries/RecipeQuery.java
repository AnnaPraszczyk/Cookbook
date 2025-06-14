package com.ania.cookbook.domain.queries;

public sealed interface RecipeQuery {

    record ForNameAndCategory(String name, String category)
            implements RecipeQuery {}
    record ForName(String name)
            implements RecipeQuery {}
    record ForCategory(String category)
        implements RecipeQuery {}

}
