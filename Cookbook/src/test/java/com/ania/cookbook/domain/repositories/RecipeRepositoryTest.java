package com.ania.cookbook.domain.repositories;

import com.ania.cookbook.domain.repositories.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

   /* @Test
    void shouldPersistRecipeAndIngredients() {
        //given
        List<Ingredient> unsavedIngredients = new ArrayList<>();
        unsavedIngredients.add(new Ingredient(500, Unit.G,"cukru"));
        Recipe unsavedRecipe = newRecipe("Ciasto", "Deser", unsavedIngredients, "None", 5);
        //when
        var savedRecipe = recipeRepository.save(unsavedRecipe);
        Optional<Recipe> retrivedRecipe = recipeRepository.findById(savedRecipe.getId());
        //then
        Assertions.assertNotNull(savedRecipe);
        Assertions.assertTrue(retrivedRecipe.isPresent());
        Assertions.assertEquals("Ciasto", retrivedRecipe.get().getName());
        Assertions.assertEquals("Deser",retrivedRecipe.get().getCategory());
        Assertions.assertEquals(5,retrivedRecipe.get().getNumberOfServings());
    }
    @Test
    void shouldFindRecipeFetchingIngerdients() {
        //given
        List<Ingredient> unsavedIngredients2 = new ArrayList<>();
        unsavedIngredients2.add(new Ingredient(200, Unit.G,"cukru"));
        Recipe recipe2 = newRecipe("Ciasto2", "Deser", unsavedIngredients2, "None", 10);
        List<String> ingredientsNames = unsavedIngredients2.stream()
                .map(Ingredient::getName)
                .toList();
        var savedRecipe2 = recipeRepository.save(recipe2);
        //when
        //var foundRecipe2 = recipeRepository.findRecipeByName(savedRecipe2.getName());
        //then
        //Assertions.assertTrue(foundRecipe2.isPresent());
        //Assertions.assertEquals("Ciasto2", foundRecipe2.map(Recipe::getName).orElseThrow());
    }
    @Test
    void shouldNotFindRecipeByName() {
        //given
        //when
        //Optional<Recipe> maybeRecipe=recipeRepository.findRecipeByName("Ciasto3");
        //then
        //Assertions.assertTrue(maybeRecipe.isEmpty());
    }*/
}