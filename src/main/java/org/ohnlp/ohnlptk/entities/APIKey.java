package org.ohnlp.ohnlptk.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.NaturalId;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an API Key Associated with a Specific User
 */
@Entity
@Table(name = "API_KEYS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
public class APIKey {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    @NaturalId
    private User user;

    @Column
    @NaturalId
    private String token;

    @Column
    private String name;

    protected APIKey() {}

    public APIKey(User user, String name, String token) {
        this.token = token;
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APIKey apiKey = (APIKey) o;
        return Objects.equals(id, apiKey.id) && Objects.equals(user, apiKey.user) && Objects.equals(token, apiKey.token) && Objects.equals(name, apiKey.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, token, name);
    }
}
