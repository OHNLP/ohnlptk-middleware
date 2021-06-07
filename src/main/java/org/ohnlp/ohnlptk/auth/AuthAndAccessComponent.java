package org.ohnlp.ohnlptk.auth;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthAndAccessComponent {
    private final UserRepository repository;

    public AuthAndAccessComponent(UserRepository repository) {
        this.repository = repository;
    }

    public User getUserForSpringSecurityContextAuth(Authentication authentication) {

        String principal;
        if (authentication instanceof OAuth2AuthenticationToken) {
            principal = ((OAuth2User)authentication.getPrincipal()).getAttribute("email");
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            principal = ((OidcUser)authentication.getPrincipal()).getEmail();
        } else {
            principal = authentication.getName();
        }
        return repository.findByEmail(principal);
    }

    public boolean userCanReadRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(g -> g.isRead() || g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }

    public boolean userCanWriteRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(g -> g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }

    public boolean userCanManageRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(AuthorityGrant::isManage)
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }
}
