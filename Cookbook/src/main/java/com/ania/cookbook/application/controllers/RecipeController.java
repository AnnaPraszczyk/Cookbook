package com.ania.cookbook.application.controllers;

/*@RestController
@RequestMapping("/recipes")
class RecipeController {
    private final RecipeService recipeService;
    private final RecipeMapping.ApiMapping apiMapping;
    private final RecipeMapping.ModelMapping modelMapping;

    @Autowired
    RecipeController(RecipeService recipeService, RecipeMapping.ApiMapping apiMapping, RecipeMapping.ModelMapping modelMapping) {
        this.recipeService = recipeService;
        this.apiMapping = apiMapping;
        this.modelMapping = modelMapping;
    }*/
    /*@PostMapping
    @ResponseStatus(HttpStatus.OK)
    RecipeDetails createRecipe(@RequestBody RecipeRequest request) throws RecipeException {
        var recipe = apiMapping.map(request);
        var saveRecipe = recipeService.saveRecipe(recipe);
        return modelMapping.map(saveRecipe);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecipeDetails getRecipe(@PathVariable("id") UUID id) throws RecipeException {
        var recipe = recipeService.getRecipe(id);
        return modelMapping.map(recipe);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    Collection<RecipeDetails> getRecipesForName(@RequestParam("name") String name){
        var matchedRecipes = recipeService.getByRecipeName(name);
        return matchedRecipes.stream()
                .map(modelMapping::map)
                .toList();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecipeDetails updateRecipe(@PathVariable("id") UUID id, @RequestBody String name) throws RecipeException{
        var recipe = recipeService.changeRecipe(id, name);
        return modelMapping.map(recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRecipe(@PathVariable("id") UUID id) throws RecipeException{
        recipeService.cancel(id);
    }

    @ExceptionHandler(RecipeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleRecipeException(RecipeException e){
        return e.getMessage();
    }*/


