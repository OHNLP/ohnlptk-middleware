package org.ohnlp.ohnlptk.dto.auth;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.APIKey;

/**
 * A read-only DTO representation of {@link APIKey}.
 */
public class APIKeyDTO extends LoadableDTO<APIKey, APIKeyDTO> {

    private Long id;
    private String token;
    private String name;

    public APIKeyDTO() {
        super(APIKey.class);
    }

    @Override
    public APIKeyDTO generateFromEntity(APIKey entity) {
        this.id = entity.getId();
        this.token = entity.getToken();
        this.name = entity.getName();
        return this;
    }

    @Override
    public APIKey mergeFromDTO(APIKey existing, DTOFactory factory) {
        return existing; // Read-only
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
