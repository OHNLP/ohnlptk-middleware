package org.ohnlp.ohnlptk.controllers;

import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.APIKeyRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/api_keys")
    public Map<String, APIKey> getApiKeys(Authentication authentication) {
        Map<String, APIKey> ret = new HashMap<>();
        this.apiKeyRepository.findAPIKeysByUser(getUserFromAuth(authentication)).forEach(apiKey -> ret.put(apiKey.getName(), apiKey));
        return ret;
    }

    @PostMapping("/create_api_key")
    public APIKey create(Authentication authentication, @RequestParam("name") String name) {
        Map<String, APIKey> apiKeys = getApiKeys(authentication);
        if (apiKeys.containsKey(name)) {
            return apiKeys.get(name);
        } else {
            APIKey newKey = new APIKey(getUserFromAuth(authentication),
                    name,
                    Base64.getEncoder().encodeToString(
                            UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
            this.apiKeyRepository.save(newKey);
            return newKey;
        }
    }

    private User getUserFromAuth(Authentication authentication) {
        String principal = authentication.getPrincipal().toString();
        return this.userRepository.findByEmail(principal);
    }

}
