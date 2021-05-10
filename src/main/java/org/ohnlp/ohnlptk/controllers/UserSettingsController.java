package org.ohnlp.ohnlptk.controllers;

import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.repositories.APIKeyRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/_user")
public class UserSettingsController {
    private final APIKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserSettingsController(APIKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api_keys")
    public Collection<APIKey> getApiKeys(Authentication authentication) {
        String principal = authentication.getPrincipal().toString();
        return this.apiKeyRepository.findAPIKeysByUser(this.userRepository.findByEmail(principal));
    }

}
