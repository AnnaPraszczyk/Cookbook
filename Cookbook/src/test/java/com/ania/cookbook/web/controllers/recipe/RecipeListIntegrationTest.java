package com.ania.cookbook.web.controllers.recipe;

import com.ania.cookbook.web.recipe.RecipeListRequest;
import com.ania.cookbook.web.recipe.RecipeListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeListIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createAndGetRecipeList() {
        RecipeListRequest createRequest = RecipeListRequest.builder()
                .listName("IntegrationList")
                .build();

        ResponseEntity<Void> postResponse = restTemplate.postForEntity("/api/recipes/lists", createRequest, Void.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode(), "Expected a 200 OK status when creating the list");

        ResponseEntity<RecipeListResponse> getResponse = restTemplate.getForEntity("/api/recipes/lists/IntegrationList", RecipeListResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Expected a 200 OK status when creating the list");
        assertNotNull(getResponse.getBody(), "The response should not be null.");
        assertEquals("IntegrationList", getResponse.getBody().getListName().name(), "The list name does not match.");
    }

    /**
     * Test sprawdzający, czy przy przesłaniu pustej nazwy listy (lub samej spacji)
     * otrzymujemy błąd walidacji, tzn. odpowiedź HTTP 400.
     */
    @Test
    public void testCreateRecipeListWhenNameIsBlank() {
        // Wysyłamy żądanie z nazwą zawierającą tylko spację, aby mapping URL został znaleziony,
        // a walidacja wewnątrz rekordu ListName się uruchomi.
        RecipeListRequest request = RecipeListRequest.builder()
                .listName(" ")   // Używamy " ", aby uniknąć sytuacji brakującej ścieżki
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity("/api/recipes/lists", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Oczekiwano statusu 400 BAD_REQUEST przy błędnej nazwie");
        assertTrue(response.getBody().contains("List name cannot be null or empty."),
                "Komunikat błędu nie zawiera oczekiwanego tekstu");
    }
}
