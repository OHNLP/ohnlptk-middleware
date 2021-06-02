package org.ohnlp.ohnlptk.auth;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class AuthUtils {

    public static User getUserForSpringSecurityContextAuth(Authentication authentication, UserRepository repository) {
        String principal;
        if (authentication.getPrincipal() instanceof OidcUser) {
            principal = ((OidcUser)authentication.getPrincipal()).getEmail();
        } else {
            principal = authentication.getName();
        }
        return repository.findByEmail(principal);
    }
}
