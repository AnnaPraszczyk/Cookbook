package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.recipe.RecipeManagementService;
import com.ania.cookbook.infrastructure.mapper.RecipeListEntryMapper;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.mock;

public class RecipeListTestConfig {
    @Bean
    public RecipeMapper recipeMapper() {
        return mock(RecipeMapper.class);
    }

    @Bean
    public RecipeListEntryMapper entryMapper() {
        return mock(RecipeListEntryMapper.class);
    }

    @Bean
    public RecipeManagementService recipeManagementService() {
        return mock(RecipeManagementService.class);
    }
}
