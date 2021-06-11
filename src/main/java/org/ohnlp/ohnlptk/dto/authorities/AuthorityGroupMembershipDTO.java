package org.ohnlp.ohnlptk.dto.authorities;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.dto.user.UserReferenceDTO;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;

/**
 * DTO for a User's view of their authority group memberships (will not contain
 * backreferences back to the user to prevent circular dependencies)
 */
public class AuthorityGroupMembershipDTO extends LoadableDTO<AuthorityGroupMembership, AuthorityGroupMembershipDTO> {
    private Long id;
    private AuthorityGroupReferenceDTO authorityGroup;
    private UserReferenceDTO principal;
    private boolean isAdmin;

    public AuthorityGroupMembershipDTO() {
        super(AuthorityGroupMembership.class);
    }

    @Override
    public AuthorityGroupMembershipDTO generateFromEntity(AuthorityGroupMembership entity) {
        this.id = entity.getId();
        this.authorityGroup = new AuthorityGroupReferenceDTO().generateFromEntity(entity.getGroup());
        this.principal = new UserReferenceDTO().generateFromEntity(entity.getPrincipal());
        this.isAdmin = entity.isAdmin();
        return this;
    }

    @Override
    public AuthorityGroupMembership mergeFromDTO(AuthorityGroupMembership existing, DTOFactory dto) {
        return existing; // Users cannot edit their own memberships directly unless add/removing, code flow should go through direct entity creation
    }

    @Override
    public Object identityValue() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityGroupReferenceDTO getAuthorityGroup() {
        return authorityGroup;
    }

    public void setAuthorityGroup(AuthorityGroupReferenceDTO authorityGroup) {
        this.authorityGroup = authorityGroup;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
