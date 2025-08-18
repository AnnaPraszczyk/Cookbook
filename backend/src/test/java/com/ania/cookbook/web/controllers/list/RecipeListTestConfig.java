package com.ania.cookbook.web.controllers.list;

import com.ania.cookbook.application.services.implementations.list.ListService;
import com.ania.cookbook.infrastructure.mapper.ListEntryMapper;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.mock;

public class RecipeListTestConfig {
    @Bean
    public RecipeMapper recipeMapper() {
        return mock(RecipeMapper.class);
    }

    @Bean
    public ListEntryMapper entryMapper() {
        return mock(ListEntryMapper.class);
    }

    @Bean
    public ListService recipeManagementService() {
        return mock(ListService.class);
    }
}
