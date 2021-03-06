package org.ohnlp.ohnlptk.dto.user;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.dto.auth.APIKeyReferenceDTO;
import org.ohnlp.ohnlptk.dto.authorities.AuthorityGroupMembershipDTO;
import org.ohnlp.ohnlptk.entities.User;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * DTO representation of a {@link User}
 */
public class UserDTO extends LoadableDTO<User, UserDTO> {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private Collection<APIKeyReferenceDTO> apiKeys;
    private Collection<AuthorityGroupMembershipDTO> groups;

    public UserDTO() {
        super(User.class);
    }

    @Override
    public UserDTO generateFromEntity(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.imageUrl = entity.getImageUrl();
        this.apiKeys = entity.getApiKeys().stream()
                .map(a -> new APIKeyReferenceDTO().generateFromEntity(a))
                .collect(Collectors.toList());
        this.groups = entity.getGroups().stream()
                .map(a -> new AuthorityGroupMembershipDTO().generateFromEntity(a))
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public User mergeFromDTO(User existing, DTOFactory factory) {
        // No changes allowed for id, name, email, and imageUrl
        existing.setApiKeys(this.apiKeys.stream().map(factory::mergeOrCreate)
                .peek(a -> a.setUser(existing))
                .collect(Collectors.toList()));
        existing.setGroups(this.groups.stream().map(factory::mergeOrCreate)
                .peek(a -> a.setPrincipal(existing))
                .collect(Collectors.toList()));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Collection<APIKeyReferenceDTO> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Collection<APIKeyReferenceDTO> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Collection<AuthorityGroupMembershipDTO> getGroups() {
        return groups;
    }

    public void setGroups(Collection<AuthorityGroupMembershipDTO> groups) {
        this.groups = groups;
    }
}
