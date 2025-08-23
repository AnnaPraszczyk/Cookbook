package com.ania.cookbook.web.controllers.list;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.model.*;
import com.ania.cookbook.domain.repositories.recipe.SaveRecipe;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.list.ListRequest;
import com.ania.cookbook.web.list.ListResponse;
import com.ania.cookbook.web.recipe.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SaveRecipe saveRecipeRepository;

    @Autowired
    private ProductUseCase productUseCase;

    private String baseUrl;

    @BeforeAll
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/lists";
    }

    private UUID insertTestRecipe() {
        ProductName name = ProductName.from("Flour");
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
        ListRequest addRequest = ListRequest.builder()
                .listName(listName)
                .listDescription("test")
                .portions(2)
                .build();
        ResponseEntity<Void> addResponse = restTemplate.postForEntity(baseUrl, addRequest, Void.class);
        assertEquals(HttpStatus.CREATED, addResponse.getStatusCode());
        ListRequest addRecipeRequest = ListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .portions(2)
                .build();
        ResponseEntity<Void> addRecipeResponse = restTemplate.postForEntity(
                baseUrl + "/" + listName + "/recipes", addRecipeRequest, Void.class);

        assertEquals(HttpStatus.CREATED, addRecipeResponse.getStatusCode());
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + listName, ListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        ListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertEquals(1, responseBody.getRecipes().size());
        assertEquals(recipeId, responseBody.getRecipes().getFirst().getRecipe().recipeId());
    }

    @Test
    void createRecipeListWhenNameIsBlank() {
        ListRequest request = ListRequest.builder()
                .listName(" ")
                .listDescription("test")
                .portions(2)
                .build();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("List name cannot be blank"));
    }

    @Test
    void returnCreatedWhenCreatingList() {
        ListRequest request = ListRequest.builder()
                .listName(generateUniqueListName())
                .portions(2)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, request, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createEmptyRecipeListAndRetrieveIt() {
        String listName = generateUniqueListName();
        ListRequest createRequest = ListRequest.builder()
                .listName(listName)
                .listDescription("test")
                .portions(2)
                .build();
        ResponseEntity<Void> postResponse = restTemplate.postForEntity(baseUrl, createRequest, Void.class);

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + listName, ListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        ListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertTrue(responseBody.getRecipes().isEmpty());
    }

    @Test
    void createRecipeListAndAddRecipe_thenRetrieveIt() {
        String listName = generateUniqueListName();
        ListRequest createRequest = ListRequest.builder()
                .listName(listName)
                .listDescription("test")
                .portions(2)
                .build();
        restTemplate.postForEntity(baseUrl, createRequest, Void.class);
        UUID recipeId = insertTestRecipe();
        ListRequest addRecipeRequest = ListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .listDescription("test")
                .portions(2)
                .build();
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + listName, ListResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        ListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertEquals(1, responseBody.getRecipes().size());
        assertEquals(recipeId, responseBody.getRecipes().getFirst().getRecipe().recipeId());
    }

    @Test
    void addingNonExistentRecipeShouldReturnNotFound() {
        String listName = generateUniqueListName();
        ListRequest createListRequest = ListRequest.builder()
                .listName(listName)
                .listDescription("Lunch list")
                .portions(2)
                .build();
        restTemplate.postForEntity(baseUrl, createListRequest, Void.class);
        UUID fakeRecipeId = UUID.randomUUID();
        ListRequest addRecipeRequest = ListRequest.builder()
                .recipeId(fakeRecipeId)
                .listName(listName)
                .listDescription("new")
                .portions(4)
                .build();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                addRecipeRequest, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Recipe with given Id does not exist."));
    }

    @Test
    void addingRecipeToDeletedListShouldReturnNotFound() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl, ListRequest.builder().listName(listName).build(), Void.class);
        restTemplate.exchange(baseUrl + "/" + listName, HttpMethod.DELETE, null, Void.class);
        UUID recipeId = insertTestRecipe();
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                ListRequest.builder().recipeId(recipeId).listName(listName).listDescription("New list")
                        .portions(4).build(), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllListsShouldIncludeCreatedList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        ResponseEntity<String[]> response =
                restTemplate.getForEntity(baseUrl, String[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<String> lists = Arrays.asList(response.getBody());
        assertTrue(lists.contains(listName));
    }

    @Test
    void getAllListsShouldReturnCreatedListNames() {
        String listName1 = generateUniqueListName();
        String listName2 = generateUniqueListName();
        restTemplate.postForEntity(baseUrl, ListRequest.builder().listName(listName1)
                        .listDescription("test").portions(2).build(), Void.class);
        restTemplate.postForEntity(baseUrl, ListRequest.builder().listName(listName2)
                        .listDescription("test").portions(2).build(), Void.class);
        ResponseEntity<List<String>> response = restTemplate.exchange(
                baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> names = response.getBody();
        assertNotNull(names);
        assertTrue(names.contains(listName1));
        assertTrue(names.contains(listName2));
    }

    @Test
    void removeRecipeFromListShouldDeleteItFromListAndShoppingList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        IngredientRequest flour = new IngredientRequest("Flour", 300F, Unit.G);
        RecipeRequest recipeRequest = new RecipeRequest(
                "Cake", Category.DESSERT,
                List.of(flour), "Bake", 4, List.of("sweet"));
        UUID recipeId = Objects.requireNonNull(restTemplate.postForEntity("/api/recipes", recipeRequest, RecipeResponse.class).getBody()).recipeId();

        ResponseEntity<ListEntryResponse> entryResponse = restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                ListRequest.builder()
                        .recipeId(recipeId)
                        .listName(listName)
                        .listDescription("test")
                        .portions(4)
                        .build(),
                ListEntryResponse.class
        );
        UUID entryId = Objects.requireNonNull(entryResponse.getBody()).getEntryId();
        restTemplate.exchange(
                baseUrl + "/" + listName + "/entries/" + entryId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + listName, ListResponse.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().getRecipes().isEmpty());

        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                baseUrl + "/" + listName + "/shopping", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(shoppingResponse.getBody());
        assertTrue(shoppingResponse.getBody().isEmpty());
    }

    @Test
    void clearRecipeListShouldReturnTrue() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        ListRequest addRecipeRequest = ListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .listDescription("test")
                .portions(2)
                .build();
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                baseUrl + "/" + listName + "/clear?confirm=true",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void clearRecipeListShouldRemoveAllRecipes() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        ListRequest addRecipeRequest = ListRequest.builder()
                .recipeId(recipeId)
                .listName(listName)
                .portions(2)
                .build();
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes", addRecipeRequest, Void.class);
        ResponseEntity<Boolean> clearResponse = restTemplate.exchange(
                baseUrl + "/" + listName + "/clear?confirm=true",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, clearResponse.getStatusCode());
        assertEquals(Boolean.TRUE, clearResponse.getBody());
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + listName, ListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        ListResponse responseBody = getResponse.getBody();
        assertNotNull(responseBody);
        assertEquals(listName, responseBody.getListName().name());
        assertTrue(responseBody.getRecipes().isEmpty());
    }

    @Test
    void clearRecipeListWithoutConfirmationShouldNotClearList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl, ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                ListRequest.builder().recipeId(recipeId).listName(listName).listDescription("new").portions(4).build(), Void.class);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                baseUrl + "/" + listName + "/clear?confirm=false",
                HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.FALSE, response.getBody());
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + listName, ListResponse.class);
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().getRecipes().size());
    }

    @Test
    void deleteRecipeList() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test").portions(2).build(), Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + listName,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletedListShouldReturnNotFound() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        restTemplate.exchange(baseUrl + "/" + listName,
                HttpMethod.DELETE, null, Void.class);
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + listName, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Recipe list with the given name does not exist."));
    }

    @Test
    void generateShoppingListShouldIncludeIngredients() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(),Void.class);
        IngredientRequest flour = new IngredientRequest("Flour", 300F, Unit.G);
        RecipeRequest recipeRequest = new RecipeRequest(
                "Test Cake", Category.DESSERT,
                List.of(flour), "Mix and Bake", 4, List.of("sweet")
        );
        ResponseEntity<RecipeResponse> createdRecipeResponse =
                restTemplate.postForEntity("/api/recipes", recipeRequest, RecipeResponse.class);
        assertNotNull(createdRecipeResponse.getBody());
        UUID recipeId = createdRecipeResponse.getBody().recipeId();
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                new ListRequest(recipeId,listName,null,2,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse =
                restTemplate.exchange(baseUrl + "/" + listName + "/shopping", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        Map<String, Float> result = shoppingResponse.getBody();

        assertEquals(HttpStatus.OK, shoppingResponse.getStatusCode());
        assertNotNull(result);
        assertTrue(result.keySet().stream().anyMatch(k -> k.equalsIgnoreCase("Flour")));
        assertEquals(150.0, Double.parseDouble(result.get("Flour").toString()));
    }

    @Test
    void generateShoppingListShouldSumDuplicateIngredients() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
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
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                new ListRequest(recipeId1, listName, null,4,null), Void.class);
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                new ListRequest(recipeId2, listName, null,4,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                baseUrl + "/" + listName + "/shopping",
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
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
        UUID recipeId = insertTestRecipe();
        ResponseEntity<Void> firstResponse = restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                ListRequest.builder()
                        .recipeId(recipeId)
                        .listName(listName)
                        .portions(4)
                        .build(),
                Void.class);

        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
        ResponseEntity<Void> secondResponse = restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                ListRequest.builder()
                        .recipeId(recipeId)
                        .listName(listName)
                        .portions(4)
                        .build(),
                Void.class);
        assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());
        ResponseEntity<ListResponse> getResponse = restTemplate.getForEntity(baseUrl + "/" + listName, ListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().getRecipes().size());
    }
    @Test
    void generateShoppingListShouldScaleIngredientsByPortions() {
        String listName = generateUniqueListName();
        restTemplate.postForEntity(baseUrl,
                ListRequest.builder().listName(listName).listDescription("test")
                .portions(2).build(), Void.class);
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
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                new ListRequest(recipeId1, listName, null,2,null), Void.class);
        restTemplate.postForEntity(baseUrl + "/" + listName + "/recipes",
                new ListRequest(recipeId2, listName, null,4,null), Void.class);
        ResponseEntity<Map<String, Float>> shoppingResponse = restTemplate.exchange(
                baseUrl + "/" + listName + "/shopping",
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
