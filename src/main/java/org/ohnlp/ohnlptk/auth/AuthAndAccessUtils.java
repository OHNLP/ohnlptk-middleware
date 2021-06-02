package org.ohnlp.ohnlptk.auth;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.stream.Collectors;

public class AuthAndAccessUtils {

    public static User getUserForSpringSecurityContextAuth(Authentication authentication, UserRepository repository) {
        String principal;
        if (authentication.getPrincipal() instanceof OidcUser) {
            principal = ((OidcUser)authentication.getPrincipal()).getEmail();
        } else {
            principal = authentication.getName();
        }
        return repository.findByEmail(principal);
    }

    public static boolean userCanReadRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(g -> g.isRead() || g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }

    public static boolean userCanWriteRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(g -> g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }

    public static boolean userCanManageRuleset(User u, RuleSetDefinition def) {
        return def.getGrants().stream()
                .filter(AuthorityGrant::isManage)
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u);
    }
}
