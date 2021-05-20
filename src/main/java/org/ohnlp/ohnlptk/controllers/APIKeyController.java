package org.ohnlp.ohnlptk.controllers;

import io.swagger.annotations.ApiOperation;
import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.APIKeyRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.ohnlp.ohnlptk.auth.AuthUtils.getUserForSpringSecurityContextAuth;

/**
 * Handles API Key Management for CLI/Non-SAML/Non-OAuth Authentication
 */
@Controller
@RequestMapping("/_apiauth")
public class APIKeyController {
    private final APIKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Autowired
    public APIKeyController(APIKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }


    @ApiOperation("Gets a mapping of name -> API Keys for the authenticated user")
    @GetMapping("/api_keys")
    public Map<String, APIKey> getApiKeys(Authentication authentication) {
        Map<String, APIKey> ret = new HashMap<>();
        this.apiKeyRepository.findAPIKeysByUser(getUserForSpringSecurityContextAuth(authentication, this.userRepository))
                .forEach(apiKey -> ret.put(apiKey.getName(), apiKey));
        return ret;
    }

    @ApiOperation("Creates an API key with the given name for the authenticated user, " +
            "returns the API key if it already exists")
    @PostMapping("/create_api_key")
    public APIKey create(Authentication authentication, @RequestParam("name") String name) {
        Map<String, APIKey> apiKeys = getApiKeys(authentication);
        if (apiKeys.containsKey(name)) {
            return apiKeys.get(name);
        } else {
            APIKey newKey = new APIKey(getUserForSpringSecurityContextAuth(authentication, this.userRepository),
                    name,
                    Base64.getEncoder().encodeToString(
                            UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            newKey = this.apiKeyRepository.save(newKey);
            return newKey;
        }
    }

    @ApiOperation("Deletes the API Key with the given uid value, returns the current mapping of existing api keys")
    @DeleteMapping("/delete_key")
    public ResponseEntity<Map<String, APIKey>> delete(Authentication authentication, @RequestParam("token") String token) {
        APIKey key = this.apiKeyRepository.findAPIKeyByToken(token);
        if (key == null || !key.getUser().getEmail().equalsIgnoreCase(authentication.getPrincipal().toString())) {
            return ResponseEntity.notFound().build();
        } else {
            this.apiKeyRepository.delete(key);
            return ResponseEntity.ok(getApiKeys(authentication));
        }
    }


}
