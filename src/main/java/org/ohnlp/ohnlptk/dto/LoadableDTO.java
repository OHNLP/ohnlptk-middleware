package org.ohnlp.ohnlptk.dto;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Abstract class extended by data transfer object views to convert to/from DTOs from hibernate entities
 *
 * Note that persistence/saving is handled within the abstract class itself and does not need to be done
 * in implementations for the ENTITY_TYPE itself.
 *
 * @param <ENTITY_TYPE> The hibernate entity type
 * @param <DTO_TYPE> The data transfer object type
 */
public abstract class LoadableDTO<ENTITY_TYPE, DTO_TYPE> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<ENTITY_TYPE> entityTypeClazz;
    private Field idField;
    private Constructor<ENTITY_TYPE> ctor;

    protected LoadableDTO(Class<ENTITY_TYPE> entityTypeClazz) {
        this.entityTypeClazz = entityTypeClazz;
        for (Field field : this.entityTypeClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                this.idField = field;
                break;
            }
        }
        if (this.idField == null) {
            throw new IllegalArgumentException("An entity type without a JPA-managed ID was supplied");
        }
        try {
            this.ctor = this.entityTypeClazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Could not find a 0-arg constructor for the declared entity");
        }
        this.ctor.setAccessible(true);
    }


    /**
     * Creates a DTO view of type DTO_TYPE from the given entity
     * @param entity The entity to create a DTO for
     * @return The created DTO
     */
    public abstract DTO_TYPE generateFromEntity(ENTITY_TYPE entity);


    /**
     * Merges a DTO view into an existing hibernate entity representation
     * @param existing The hibernate entity representation
     * @param dto THe dto entity representation
     * @return The merged entity
     */
    protected abstract ENTITY_TYPE mergeFromDTO(ENTITY_TYPE existing, DTO_TYPE dto);

    /**
     * @return The identity value as represented in the DTO, typically corresponding to JPA's @Id
     * annotation. Can be null if this is a new instance
     */
    public abstract Object identityValue();

    /**
     * Does the actual merging of DTOs and/or creation of new instances as appropriate, and handles saving
     *
     * @param dto The DTO to create/merge an entity for
     * @return The final entity as saved via hibernate
     */
    public ENTITY_TYPE mergeFromDTO(DTO_TYPE dto) {
        // Check to see if an existing entity exists and create if not
        ENTITY_TYPE entity;
        if (identityValue() != null) {
            entity = this.entityManager.find(entityTypeClazz, identityValue());
            if (entity == null) {
                throw new IllegalArgumentException("Supplied a DTO with an identity value that does not exist in JPA Repository");
            }
        } else {
            try {
                entity = this.ctor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Failed to invoke entity constructor", e);
            }
            this.entityManager.persist(entity);
        }
        // Merge in DTO
        entity = mergeFromDTO(entity, dto);
        // Save
        entity = this.entityManager.merge(entity);
        return entity;
    }

}
