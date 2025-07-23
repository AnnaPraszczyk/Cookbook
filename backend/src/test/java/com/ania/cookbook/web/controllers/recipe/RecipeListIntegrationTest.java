package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.recipe.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeListIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SaveRecipe saveRecipeRepository;

    @Autowired
    private ProductUseCase productUseCase;

    private UUID insertTestRecipe() {
        ProductName name = ProductName.from("Mąka");
        Product product = productUseCase.findProductByName(name)
                .orElseGet(() -> productUseCase.addProduct(name));
        Ingredient ingredient = Ingredient.newIngredient(product, 200f, Unit.G);
        Recipe recipe = Recipe.newRecipe(
                UUID.randomUUID(),
                "Test",
                Category.DESSERT,
                List.of(ingredient),
                "Mix and bake",
                4,
                List.of("fast", "easy")
        );
        return saveRecipeRepository.saveRecipe(recipe).getRecipeId();
    }

    private String generateUniqueListName() {
        return "List_" + UUID.randomUUID();
    }

    @Test
    void createListAndAddRecipe() {
        String listName = generateUniqueListName();
        UUID recipeId = insertTestRecipe();
        System.out.println("➡️ recipeId: " + recipeId);
        RecipeListRequest addRequest = RecipeListRequest.builder()
                .listName(listName)
                .build();
        String url = "http://localhost:" + port + "/api/lists";
        ResponseEntity<Void> addResponse = restTemplate.postForEntity(url, addRequest, Void.class);
        assertEquals(HttpStatus.CREATED, addResponse.getStatusCode());
        RecipeListRequest addRecipeRequest = RecipeListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .build();
        ResponseEntity<Void> addRecipeResponse = restTemplate.postForEntity(
                "/api/lists/" + listName + "/recipes", addRecipeRequest, Void.class);

        assertEquals(HttpStatus.CREATED, addRecipeResponse.getStatusCode());
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        RecipeListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertEquals(1, responseBody.getRecipes().size());
        assertEquals(recipeId, responseBody.getRecipes().getFirst().getId());
    }

    @Test
    void createRecipeListWhenNameIsBlank() {
        RecipeListRequest request = RecipeListRequest.builder()
                .listName(" ")
                .build();
        ResponseEntity<String> response = restTemplate.postForEntity("/api/lists", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("List name cannot be null or empty."));
    }

    @Test
    void createEmptyRecipeListAndRetrieveIt() {
        String listName = generateUniqueListName();
        RecipeListRequest createRequest = RecipeListRequest.builder()
                .listName(listName)
                .build();
        ResponseEntity<Void> postResponse = restTemplate.postForEntity("/api/lists", createRequest, Void.class);

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        RecipeListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertTrue(responseBody.getRecipes().isEmpty());
    }

    @Test
    void createRecipeListAndAddRecipe_thenRetrieveIt() {
        String listName = generateUniqueListName();
        RecipeListRequest createRequest = RecipeListRequest.builder()
                .listName(listName)
                .build();
        restTemplate.postForEntity("/api/lists", createRequest, Void.class);
        UUID recipeId = insertTestRecipe();
        RecipeListRequest addRecipeRequest = RecipeListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .build();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        RecipeListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertEquals(1, responseBody.getRecipes().size());
        assertEquals(recipeId, responseBody.getRecipes().getFirst().getId());
    }

    @Test
    void addingNonExistentRecipeShouldReturnNotFound() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists", RecipeListRequest.builder().listName(listName).build(), Void.class);
        UUID fakeRecipeId = UUID.randomUUID();
        ResponseEntity<String> response = restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder().recipeId(fakeRecipeId).listName(listName).portions(4).build(),
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Recipe not found"));
    }

    @Test
    void addingRecipeToDeletedListShouldReturnNotFound() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists", RecipeListRequest.builder().listName(listName).build(), Void.class);
        restTemplate.exchange("/api/lists/" + listName, HttpMethod.DELETE, null, Void.class);
        UUID recipeId = insertTestRecipe();
        ResponseEntity<String> response = restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder().recipeId(recipeId).listName(listName).portions(4).build(),
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllListsShouldIncludeCreatedList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        ResponseEntity<String[]> response =
                restTemplate.getForEntity("/api/lists", String[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<String> lists = Arrays.asList(response.getBody());
        assertTrue(lists.contains(listName));
    }

    @Test
    void getAllListsShouldReturnCreatedListNames() {
        String listName1 = generateUniqueListName();
        String listName2 = generateUniqueListName();
        restTemplate.postForEntity("/api/lists", RecipeListRequest.builder().listName(listName1).build(), Void.class);
        restTemplate.postForEntity("/api/lists", RecipeListRequest.builder().listName(listName2).build(), Void.class);
        ResponseEntity<List<String>> response = restTemplate.exchange(
                "/api/lists", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> names = response.getBody();
        assertNotNull(names);
        assertTrue(names.contains(listName1));
        assertTrue(names.contains(listName2));
    }

    @Test
    void removeRecipeFromListShouldDeleteItFromListAndShoppingList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        IngredientRequest flour = new IngredientRequest("Flour", 300F, Unit.G);
        RecipeRequest recipeRequest = new RecipeRequest(
                "Cake", Category.DESSERT,
                List.of(flour), "Bake", 4, List.of("sweet")
        );
        UUID recipeId = Objects.requireNonNull(restTemplate.postForEntity("/api/recipes", recipeRequest, RecipeResponse.class).getBody()).recipeId();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder().recipeId(recipeId).listName(listName).portions(4).build(), Void.class);
        restTemplate.exchange("/api/lists/" + listName + "/recipes/" + recipeId,
                HttpMethod.DELETE, null, Void.class);
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().getRecipes().isEmpty());
        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                "/api/lists/" + listName + "/shopping", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(shoppingResponse.getBody());
        assertTrue(shoppingResponse.getBody().isEmpty());
    }

    @Test
    void clearRecipeListShouldReturnTrue() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        RecipeListRequest addRecipeRequest = RecipeListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .build();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/lists/" + listName + "/clear?confirm=true",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void clearRecipeListShouldRemoveAllRecipes() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        RecipeListRequest addRecipeRequest = RecipeListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .build();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<Boolean> clearResponse = restTemplate.exchange(
                "/api/lists/" + listName + "/clear?confirm=true",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, clearResponse.getStatusCode());
        assertEquals(Boolean.TRUE, clearResponse.getBody());
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity(
                "/api/lists/" + listName, RecipeListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        RecipeListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertTrue(responseBody.getRecipes().isEmpty());
    }

    @Test
    void clearRecipeListWithoutConfirmationShouldNotClearList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists", RecipeListRequest.builder().listName(listName).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder().recipeId(recipeId).listName(listName).portions(4).build(), Void.class);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/lists/" + listName + "/clear?confirm=false",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.FALSE, response.getBody());
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().getRecipes().size());
    }

    @Test
    void deleteRecipeList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/lists/" + listName,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletedListShouldReturnNotFound() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        restTemplate.exchange("/api/lists/" + listName,
                HttpMethod.DELETE, null, Void.class);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/lists/" + listName, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Recipe list with the given name does not exist."));
    }

    @Test
    void generateShoppingListShouldIncludeIngredients() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(),Void.class);
        IngredientRequest flour = new IngredientRequest("Flour", 300F, Unit.G);
        RecipeRequest recipeRequest = new RecipeRequest(
                "Test Cake", Category.DESSERT,
                List.of(flour), "Mix and Bake", 4, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> createdRecipeResponse =
                restTemplate.postForEntity("/api/recipes", recipeRequest, RecipeResponse.class);
        assertNotNull(createdRecipeResponse.getBody());
        UUID recipeId = createdRecipeResponse.getBody().recipeId();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                new RecipeListRequest(recipeId,null,null,null,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse =
                restTemplate.exchange("/api/lists/" + listName + "/shopping", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        Map<String, Float> result = shoppingResponse.getBody();

        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(result);
        assertTrue(result.keySet().stream().anyMatch(k -> k.equalsIgnoreCase("Flour")));
        assertEquals(300.0, Double.parseDouble(result.get("Flour").toString()));
    }

    @Test
    void generateShoppingListShouldSumDuplicateIngredients() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        IngredientRequest flour1 = new IngredientRequest("Flour", 300F, Unit.G);
        RecipeRequest recipe1 = new RecipeRequest(
                "Cake One", Category.DESSERT,
                List.of(flour1), "Mix and Bake", 4, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> response1 = restTemplate.postForEntity("/api/recipes", recipe1, RecipeResponse.class);
        assertNotNull(response1.getBody());
        UUID recipeId1 = response1.getBody().recipeId();
        IngredientRequest flour2 = new IngredientRequest("Flour", 20F, Unit.DAG);
        RecipeRequest recipe2 = new RecipeRequest(
                "Cake Two", Category.DESSERT,
                List.of(flour2), "Mix and Bake", 4, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> response2 = restTemplate.postForEntity("/api/recipes", recipe2, RecipeResponse.class);
        assertNotNull(response2.getBody());
        UUID recipeId2 = response2.getBody().recipeId();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                new RecipeListRequest(recipeId1, null, null,null,null), Void.class);
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                new RecipeListRequest(recipeId2, null, null,null,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                "/api/lists/" + listName + "/shopping",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        Map<String, Float> result = shoppingResponse.getBody();

        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(result);
        assertTrue(result.containsKey("Flour"));
        float totalFlour = result.get("Flour");
        assertEquals(500.0F, totalFlour);
    }

    @Test
    void addingSameRecipeTwiceShouldBeIgnoredSilently() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        ResponseEntity<Void> firstResponse = restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder()
                        .recipeId(recipeId)
                        .listName(listName)
                        .portions(4)
                        .build(),
                Void.class);

        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
        ResponseEntity<Void> secondResponse = restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                RecipeListRequest.builder()
                        .recipeId(recipeId)
                        .listName(listName)
                        .portions(4)
                        .build(),
                Void.class);
        assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());
        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/lists/" + listName, RecipeListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().getRecipes().size());
    }
    @Test
    void generateShoppingListShouldScaleIngredientsByPortions() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity("/api/lists",
                RecipeListRequest.builder().listName(listName).build(), Void.class);
        IngredientRequest flour1 = new IngredientRequest("Flour", 400F, Unit.G);
        RecipeRequest recipe1 = new RecipeRequest(
                "Cake One", Category.DESSERT,
                List.of(flour1), "Mix and Bake", 4, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> response1 = restTemplate.postForEntity("/api/recipes", recipe1, RecipeResponse.class);
        assertNotNull(response1.getBody());
        UUID recipeId1 = response1.getBody().recipeId();
        IngredientRequest flour2 = new IngredientRequest("Flour", 20F, Unit.DAG);
        RecipeRequest recipe2 = new RecipeRequest(
                "Cake Two", Category.DESSERT,
                List.of(flour2), "Mix and Bake", 2, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> response2 = restTemplate.postForEntity("/api/recipes", recipe2, RecipeResponse.class);
        assertNotNull(response2.getBody());
        UUID recipeId2 = response2.getBody().recipeId();
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                new RecipeListRequest(recipeId1, listName, null,2,null), Void.class);
        restTemplate.postForEntity("/api/lists/" + listName + "/recipes",
                new RecipeListRequest(recipeId2, listName, null,4,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                "/api/lists/" + listName + "/shopping",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        Map<String, Float> result = shoppingResponse.getBody();

        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(result);
        assertTrue(result.containsKey("Flour"));
        float totalFlour = result.get("Flour");
        assertEquals(600.0F, totalFlour);
    }
}
