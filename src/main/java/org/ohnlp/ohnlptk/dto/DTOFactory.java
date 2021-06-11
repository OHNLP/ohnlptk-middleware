package org.ohnlp.ohnlptk.dto;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory class for autowired management and loading of data transfer objects
 */
@Component
public class DTOFactory {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Does the actual merging of DTOs and/or creation of new instances as appropriate, and handles saving
     *
     * @param dto The DTO to create/merge an entity for
     * @return The final entity as saved via hibernate
     */
    public <ENTITY_TYPE, DTO_TYPE> ENTITY_TYPE mergeOrCreate(LoadableDTO<ENTITY_TYPE, DTO_TYPE> dto) {
        // Check to see if an existing entity exists and create if not
        ENTITY_TYPE entity;
        if (dto.identityValue() != null) {
            entity = this.entityManager.find(dto.getEntityTypeClazz(), dto.identityValue());
            if (entity == null) {
                throw new IllegalArgumentException("Supplied a DTO with an identity value that does not exist in JPA Repository");
            }
        } else {
            try {
                entity = dto.getCtor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Failed to invoke entity constructor", e);
            }
            this.entityManager.persist(entity);
        }
        // Merge in DTO
        entity = dto.mergeFromDTO(entity, this);
        // Save
        entity = this.entityManager.merge(entity);
        return entity;
    }
}
