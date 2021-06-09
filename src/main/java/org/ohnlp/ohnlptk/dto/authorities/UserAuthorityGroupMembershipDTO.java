package org.ohnlp.ohnlptk.dto.authorities;

import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;

/**
 * DTO for a User's view of their authority group memberships (will not contain
 * backreferences back to the user to prevent circular dependencies)
 */
public class UserAuthorityGroupMembershipDTO extends LoadableDTO<AuthorityGroupMembership, UserAuthorityGroupMembershipDTO> {
    private Long id;
    private AuthorityGroupReferenceDTO authorityGroup;
    private boolean isAdmin;

    public UserAuthorityGroupMembershipDTO() {
        super(AuthorityGroupMembership.class);
    }

    @Override
    public UserAuthorityGroupMembershipDTO generateFromEntity(AuthorityGroupMembership entity) {
        this.id = entity.getId();
        this.authorityGroup = new AuthorityGroupReferenceDTO().generateFromEntity(entity.getGroup());
        this.isAdmin = entity.isAdmin();
        return this;
    }

    @Override
    protected AuthorityGroupMembership mergeFromDTO(AuthorityGroupMembership existing, UserAuthorityGroupMembershipDTO dto) {
        return existing; // Users cannot edit their own memberships directly unless removing, code flow should go through auth group instead
    }

    @Override
    public Object identityValue() {
        return id;
    }
}
