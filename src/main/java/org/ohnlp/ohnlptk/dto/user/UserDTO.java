package org.ohnlp.ohnlptk.dto.user;

import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.dto.auth.APIKeyDTO;
import org.ohnlp.ohnlptk.dto.authorities.UserViewAuthorityGroupMembershipDTO;
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
    private Collection<APIKeyDTO> apiKeys;
    private Collection<UserViewAuthorityGroupMembershipDTO> groups;

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
                .map(a -> new APIKeyDTO().generateFromEntity(a))
                .collect(Collectors.toList());
        this.groups = entity.getGroups().stream()
                .map(a -> new UserViewAuthorityGroupMembershipDTO().generateFromEntity(a))
                .collect(Collectors.toList());
        return this;
    }

    @Override
    protected User mergeFromDTO(User existing, UserDTO dto) {
        // No changes allowed for id, name, email, and imageUrl
        existing.setApiKeys(dto.apiKeys.stream().map(a -> a.mergeFromDTO(a))
                .peek(a -> a.setUser(existing))
                .collect(Collectors.toList()));
        existing.setGroups(dto.groups.stream().map(a -> a.mergeFromDTO(a))
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

    public Collection<APIKeyDTO> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Collection<APIKeyDTO> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Collection<UserViewAuthorityGroupMembershipDTO> getGroups() {
        return groups;
    }

    public void setGroups(Collection<UserViewAuthorityGroupMembershipDTO> groups) {
        this.groups = groups;
    }
}
