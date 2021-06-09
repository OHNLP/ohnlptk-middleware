package org.ohnlp.ohnlptk.test.controllers;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.ohnlp.ohnlptk.controllers.APIKeyController;
import org.ohnlp.ohnlptk.entities.APIKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * Tests REST endpoints under the /_apiauth path.
 * Note that tests here are done using direct java calls to relevant functions and not by doing the REST calls themselves.
 * We do testing via REST calls elsewhere during full-stack integration testing.
 */
public class APIKeyControllerUnitTests extends AuthenticatedControllerTest {

    @Autowired
    private APIKeyController apiKeyController;

    private String generatedAPIToken;

    /**
     * Tests the /create_api_key REST endpoint
     * Creates an API key associated with user 1 and verifies that the required fields are populated.
     *
     * Once this function is confirmed working, creates another API key on user 1 and an API key associated with user 2
     * for later use in other tests.
     */
    @Test
    @Order(1)
    public void createApiKeyTest() {
        APIKey resp = this.apiKeyController.create(this.mockUserAuth, "User 1 API Key");
        Assert.isTrue(resp.getUser().getEmail().equals(this.mockUserAuth.getName()), "User mismatch in created API key");
        Assert.isTrue(resp.getName().equals("User 1 API Key"), "Created API Key Name Mismatch");
        Assert.notNull(resp.getToken(), "Generated API Key Token is null");
        Assert.notNull(resp.getId(), "Generated API Key lacks a hibernate entity ID");
        this.apiKeyController.create(this.mockUserAuth2, "User 2 API Key");
        this.apiKeyController.create(this.mockUserAuth, "User 1 API Key 2");
        this.generatedAPIToken = resp.getToken();
    }

    /**
     * Test the /api_keys REST endpoint.
     * Retrieves user 1's API keys, and ensures there are two entries and that the retrieved entries match by name
     */
    @Test
    @Order(2)
    public void apiKeysTest() {
        Map<String, APIKey> resp = this.apiKeyController.getApiKeys(this.mockUserAuth);
        Assert.isTrue(!resp.containsKey("User 2 API Key"), "User 2's API Keys were returned for user 1");
        Assert.isTrue(resp.size() == 2, "Expected 2 api keys for user 1, got " + resp.size());
        Assert.isTrue(resp.containsKey("User 1 API Key"), "User 1's API Key #1 not populated in returned results");
        Assert.isTrue(resp.containsKey("User 1 API Key 2"), "User 1's API Key #2 not populated in returned results");
    }

    /**
     * Test the /delete_key REST endpoint
     *
     * First tries to delete user 1's api key using user 2's authentication and fails if successful
     * Then deletes using user 1's credentials and retrieves user 1's API keys to ensure that it is removed
     */
    @Test
    @Order(3)
    public void deleteKeyTest() {
        ResponseEntity<Map<String, APIKey>> resp = this.apiKeyController.delete(this.mockUserAuth2, this.generatedAPIToken);
        Assert.isTrue(resp.getStatusCode().isError(), "API Key Deletion Successful using Wrong User");
        Assert.isTrue(resp.getStatusCode().equals(HttpStatus.NOT_FOUND), "Return code other than 404 for " +
                "bad auth deletion, may leak presence of sensitive information and is vulnerable to dictionary attacks");
        resp = this.apiKeyController.delete(this.mockUserAuth, this.generatedAPIToken);
        Assert.isTrue(resp.getStatusCode().is2xxSuccessful(), "Return code for correct deletion is other than successful");
        Map<String, APIKey> keyMap = Objects.requireNonNull(resp.getBody());
        Assert.isTrue(keyMap.size() == 1, "Expected only 1 element in returned API key listing");
        Assert.isTrue(!keyMap.containsKey("User 1 API Key"), "User 1's API Key #1 still populated in returned results despite deletion");
        Assert.isTrue(keyMap.containsKey("User 1 API Key 2"), "User 1's API Key #2 was incorrectly removed");
    }

}
