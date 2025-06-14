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

    @Test
    public void createRecipeListWhenNameIsBlank() {
        RecipeListRequest request = RecipeListRequest.builder()
                .listName(" ")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity("/api/recipes/lists", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected 400 BAD_REQUEST status for an invalid name.");
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("List name cannot be null or empty."),
                "The error message does not contain the expected text.");
    }
}
