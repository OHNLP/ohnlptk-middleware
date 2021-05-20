package org.ohnlp.ohnlptk.auth.oidc;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroup;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Locale;

/**
 * Registers users created via OIDC requests to the local user repository
 */
@Service
public class OIDCUserRegistrationService extends OidcUserService {

    private final UserRepository userRepository;

    @Autowired
    public OIDCUserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = super.loadUser(userRequest);
        String principal = user.getEmail();
        String name = user.getName();
        loadUserLocal(principal, name);
        return user;
    }

    private void loadUserLocal(String principal, String name) {
        User user = this.userRepository.findByEmail(principal);
        if (user == null) {
            user = new User(name, principal);
            // Create default user-specific authority group with no admin and user as only member
            AuthorityGroup memberGroup = new AuthorityGroup();
            memberGroup.setName(principal.toLowerCase(Locale.ROOT));
            AuthorityGroupMembership membership = new AuthorityGroupMembership();
            membership.setAdmin(false);
            membership.setGroup(memberGroup);
            membership.setPrincipal(user);
            memberGroup.setMembers(Collections.singleton(membership));
            user.setMemberGroup(memberGroup);
        }
        this.userRepository.save(user);
    }
}
