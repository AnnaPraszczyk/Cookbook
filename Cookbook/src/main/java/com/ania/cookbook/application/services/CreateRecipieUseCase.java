package com.ania.cookbook.application.services;

import java.util.List;

public interface CreateRecipieUseCase {
    void createRecipie(CreateRecipe command);

    record CreateRecipe(
            String name,
            List<String> tagas
    ) {
    }

}
