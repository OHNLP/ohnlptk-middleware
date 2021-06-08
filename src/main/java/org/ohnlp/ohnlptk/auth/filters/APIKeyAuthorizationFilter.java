package org.ohnlp.ohnlptk.auth.filters;

import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.repositories.APIKeyRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

/**
 * This filter checks against user principal/api-key database to verify access
 */
@Component
public class APIKeyAuthorizationFilter extends GenericFilterBean {
    private final APIKeyRepository apiKeyRepository;

    @Autowired
    public APIKeyAuthorizationFilter(APIKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        if (request.getHeader("Authorization").startsWith("Bearer")) {
            chain.doFilter(req, res);
            return; //
        }
        String authHeader = new String(Base64.getDecoder().decode(request.getHeader("Authorization")), StandardCharsets.UTF_8);
        String[] parsedAuth = authHeader.split(":");
        String email = parsedAuth[0];
        String token = parsedAuth[1];
        APIKey apiKey = apiKeyRepository.findAPIKeyByToken(token);
        if (apiKey == null || !apiKey.getUser().getEmail().equalsIgnoreCase(email)) { // Token not recognized or belongs to wrong user
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
            return;
        }
        User user = apiKey.getUser();
        // Manually construct authentication details for user
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList()));
        chain.doFilter(req, res);
    }

}
