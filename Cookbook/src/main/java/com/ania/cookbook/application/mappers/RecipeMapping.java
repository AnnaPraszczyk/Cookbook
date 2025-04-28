package com.ania.cookbook.application.mappers;

public interface RecipeMapping {

    /*@Component
    class ApiMapping {

        private final ReadRecipeService readRecipeService;

        @Autowired
        private ApiMapping(ReadRecipeService readRecipeService) {
            this.readRecipeService = readRecipeService;
        }

        public Recipe map(RecipeRequest request) {
            var maybeRecipe = readRecipeService.getById(request.id());
            return Recipe.newRecipe(maybeRecipe.orElse(null),
                    request.name(), request.category(),
                    request.ingredients(), request.description(),
                    request.numberOfServings());
        }
    }
        @Component
        class ModelMapping {
        public RecipeDetails map(Recipe recipe) {
            Recipe.getId();
            Recipe.getName();
            Recipe.getCategory();
            Recipe.getIngredients();
            Recipe.getDescription();
            Recipe.Created();
            Recipe.getNumberOfServings();
        }
        }

    }*/
}