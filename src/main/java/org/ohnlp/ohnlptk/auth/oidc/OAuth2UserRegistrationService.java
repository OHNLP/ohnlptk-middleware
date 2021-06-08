package org.ohnlp.ohnlptk.auth.oidc;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroup;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;
import org.ohnlp.ohnlptk.repositories.AuthorityGroupMembershipRepository;
import org.ohnlp.ohnlptk.repositories.AuthorityGroupRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Locale;

/**
 * Registers users created via OIDC requests to the local user repository
 */
@Service
public class OAuth2UserRegistrationService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthorityGroupRepository authorityGroupRepository;
    private final AuthorityGroupMembershipRepository authorityGroupMembershipRepository;

    @Autowired
    public OAuth2UserRegistrationService(UserRepository userRepository, AuthorityGroupRepository authorityGroupRepository,
                                         AuthorityGroupMembershipRepository authorityGroupMembershipRepository) {
        this.userRepository = userRepository;
        this.authorityGroupRepository = authorityGroupRepository;
        this.authorityGroupMembershipRepository = authorityGroupMembershipRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        String principal = user.getAttribute("email");
        String name = user.getAttribute("name");
        String picture = user.getAttribute("avatar_url");
        loadUserLocal(principal, name, picture);
        return user;
    }

    public void loadUserLocal(String principal, String name, String picture) {
        User user = this.userRepository.findByEmail(principal);
        if (user == null) {
            user = new User(name, principal);
            user.setImageUrl(picture);
            user = this.userRepository.save(user);
            // Create default user-specific authority group with no admin and user as only member
            AuthorityGroup memberGroup = new AuthorityGroup();
            memberGroup.setName(principal.toLowerCase(Locale.ROOT));
            AuthorityGroupMembership membership = new AuthorityGroupMembership();
            membership.setAdmin(false);
            membership.setGroup(memberGroup);
            membership.setPrincipal(user);
            memberGroup.setMembers(Collections.singleton(membership));
            this.authorityGroupRepository.save(memberGroup);
        }
    }
}
