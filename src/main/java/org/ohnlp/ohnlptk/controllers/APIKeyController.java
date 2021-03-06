package org.ohnlp.ohnlptk.controllers;

import io.swagger.annotations.ApiOperation;
import org.ohnlp.ohnlptk.auth.AuthAndAccessComponent;
import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.auth.APIKeyDTO;
import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.repositories.APIKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles API Key Management for CLI/Non-SAML/Non-OAuth Authentication
 */
@Controller
@RequestMapping("/_apiauth")
public class APIKeyController {
    private final APIKeyRepository apiKeyRepository;
    private final AuthAndAccessComponent authAndAccessComponent;
    private final DTOFactory dtoFactory;


    @Autowired
    public APIKeyController(APIKeyRepository apiKeyRepository, AuthAndAccessComponent authAndAccessComponent,
                            DTOFactory dtoFactory) {
        this.apiKeyRepository = apiKeyRepository;
        this.authAndAccessComponent = authAndAccessComponent;
        this.dtoFactory = dtoFactory;
    }

    @ApiOperation("Creates an API key with the given name for the authenticated user, " +
            "returns the API key if it already exists")
    @PostMapping("/create_api_key")
    public @ResponseBody
    APIKeyDTO create(@ApiIgnore Authentication authentication, @RequestParam("name") String name) {
        Map<String, APIKeyDTO> apiKeys = getApiKeys(authentication);
        if (apiKeys.containsKey(name)) {
            return apiKeys.get(name);
        } else {
            APIKey newKey = new APIKey(this.authAndAccessComponent.getUserForSpringSecurityContextAuth(authentication),
                    name,
                    Base64.getEncoder().encodeToString(
                            UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            newKey = this.apiKeyRepository.save(newKey);
            return new APIKeyDTO().generateFromEntity(newKey);
        }
    }

    @ApiOperation("Gets a mapping of name -> API Keys for the authenticated user")
    @GetMapping("/api_keys")
    public @ResponseBody Map<String, APIKeyDTO> getApiKeys(@ApiIgnore Authentication authentication) {
        Map<String, APIKeyDTO> ret = new HashMap<>();
        this.apiKeyRepository.findAPIKeysByUser(this.authAndAccessComponent.getUserForSpringSecurityContextAuth(authentication))
                .forEach(apiKey -> ret.put(apiKey.getName(), new APIKeyDTO().generateFromEntity(apiKey)));
        return ret;
    }


    @ApiOperation("Deletes the API Key with the given uid value, returns the current mapping of existing api keys")
    @DeleteMapping("/delete_key")
    public ResponseEntity<Map<String, APIKeyDTO>> delete(@ApiIgnore Authentication authentication, @RequestParam("token") String token) {
        APIKey key = this.apiKeyRepository.findAPIKeyByToken(token);
        if (key == null || !key.getUser().getEmail().equalsIgnoreCase(authentication.getPrincipal().toString())) {
            return ResponseEntity.notFound().build();
        } else {
            this.apiKeyRepository.delete(key);
            return ResponseEntity.ok(getApiKeys(authentication));
        }
    }
}
