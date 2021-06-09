package org.ohnlp.ohnlptk.entities.resolvers;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.Objects.requireNonNull;

@Component
@Scope("prototype")
public class JPAEntityResolver extends SimpleObjectIdResolver {
    @PersistenceContext
    private EntityManager objectRepository;

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        super.bindItem(id, pojo);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Object resolved = super.resolveId(id);
        if (resolved == null) {
            resolved = _tryToLoadFromSource(id);
            bindItem(id, resolved);
        }

        return resolved;
    }

    private Object _tryToLoadFromSource(ObjectIdGenerator.IdKey idKey) {
        requireNonNull(idKey.scope, "global scope is not supported");

        String id = (String) idKey.key;
        Class<?> poType = idKey.scope;

        return objectRepository.getReference(poType, id);
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new JPAEntityResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == JPAEntityResolver.class;
    }
}