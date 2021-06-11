package org.ohnlp.ohnlptk.dto.authorities;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroup;

/**
 * DTO used to refer to an authority group that can be used for a full lookup
 */
public class AuthorityGroupReferenceDTO extends LoadableDTO<AuthorityGroup, AuthorityGroupReferenceDTO> {

    private Long id;
    private String name;
    private String groupUid;

    public AuthorityGroupReferenceDTO() {
        super(AuthorityGroup.class);
    }

    @Override
    public AuthorityGroupReferenceDTO generateFromEntity(AuthorityGroup entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.groupUid = entity.getGroupUid();
        return this;
    }

    @Override
    public AuthorityGroup mergeFromDTO(AuthorityGroup existing, DTOFactory factory) {
        return existing; // Read-Only - we don't adjust memberships here
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupUid() {
        return groupUid;
    }

    public void setGroupUid(String groupUid) {
        this.groupUid = groupUid;
    }
}
