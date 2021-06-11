package org.ohnlp.ohnlptk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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
    @JsonIgnore
    private EntityManager entityManager;
    @JsonIgnore
    private final Class<ENTITY_TYPE> entityTypeClazz;
    @JsonIgnore
    private Field idField;
    @JsonIgnore
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
     * Merges a DTO view into an existing hibernate entity representation.
     * @param existing The hibernate entity representation existing in JPA Repository
     * @param factory The factory class for any new DTO instances that need to be merged/created
     * @return The merged entity
     */
    public abstract ENTITY_TYPE mergeFromDTO(ENTITY_TYPE existing, DTOFactory factory);

    /**
     * @return The identity value as represented in the DTO, typically corresponding to JPA's @Id
     * annotation. Can be null if this is a new instance
     */
    public abstract Object identityValue();

    /**
     * @return The class of the produced entity
     */
    public Class<ENTITY_TYPE> getEntityTypeClazz() {
        return entityTypeClazz;
    }

    /**
     * @return The ID Field in the entity
     */
    public Field getIdField() {
        return idField;
    }

    /**
     * @return The entity's constructor
     */
    public Constructor<ENTITY_TYPE> getCtor() {
        return ctor;
    }
}
