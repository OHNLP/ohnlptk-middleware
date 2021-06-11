package org.ohnlp.ohnlptk.dto.authorities;

import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroup;

import java.util.stream.Collectors;

/**
 * DTO for a ruleset's view of its authority grants.
 */
public class RuleSetViewAuthorityGrantDTO extends LoadableDTO<AuthorityGrant, RuleSetViewAuthorityGrantDTO> {

    private Long id;
    private AuthorityGroupReferenceDTO principal;
    private boolean read;
    private boolean write;
    private boolean manage;

    public RuleSetViewAuthorityGrantDTO() {
        super(AuthorityGrant.class);
    }

    @Override
    public RuleSetViewAuthorityGrantDTO generateFromEntity(AuthorityGrant entity) {
        this.id = entity.getId();
        this.principal = new AuthorityGroupReferenceDTO().generateFromEntity(entity.getPrincipal());
        this.read = entity.isRead();
        this.write = entity.isWrite();
        this.manage = entity.isManage();
        return this;
    }

    @Override
    protected AuthorityGrant mergeFromDTO(AuthorityGrant existing, RuleSetViewAuthorityGrantDTO dto) {
        // ID is read-only
        AuthorityGroup grp = dto.principal.mergeFromDTO(dto.principal);
        existing.setPrincipal(grp);
        if (this.id == null || !grp.getGrants().stream().map(AuthorityGrant::getId).collect(Collectors.toSet()).contains(this.id)) {
            grp.getGrants().add(existing);
        }
        existing.setRead(this.read);
        existing.setWrite(this.write);
        existing.setManage(this.manage);
        return existing;
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

    public AuthorityGroupReferenceDTO getPrincipal() {
        return principal;
    }

    public void setPrincipal(AuthorityGroupReferenceDTO principal) {
        this.principal = principal;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }
}