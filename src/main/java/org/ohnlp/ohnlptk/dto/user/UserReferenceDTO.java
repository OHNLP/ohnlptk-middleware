package org.ohnlp.ohnlptk.dto.user;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.User;

/**
 * A bare-minimum DTO capable of referring to a {@link User}
 */
public class UserReferenceDTO extends LoadableDTO<User, UserReferenceDTO> {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;

    public UserReferenceDTO() {
        super(User.class);
    }

    @Override
    public UserReferenceDTO generateFromEntity(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.imageUrl = entity.getImageUrl();
        return this;
    }

    @Override
    public User mergeFromDTO(User existing, DTOFactory factory) {
        return existing; // References cannot be edited
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
}
