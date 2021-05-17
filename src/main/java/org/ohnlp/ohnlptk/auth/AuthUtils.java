package org.ohnlp.ohnlptk.auth;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    public static User getUserForSpringSecurityContextAuth(Authentication authentication, UserRepository repository) {
        String principal = authentication.getPrincipal().toString();
        return repository.findByEmail(principal);
    }
}
