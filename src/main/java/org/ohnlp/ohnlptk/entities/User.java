package org.ohnlp.ohnlptk.entities;

import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String imageUrl;

    @OneToMany
    @JoinColumn(name = "id")
    private Collection<APIKey> apiKeys;

    @OneToMany
    @JoinColumn(name = "id")
    private Collection<AuthorityGroupMembership> groups;


    protected User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Collection<APIKey> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Collection<APIKey> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Collection<AuthorityGroupMembership> getGroups() {
        return groups;
    }

    public void setGroups(Collection<AuthorityGroupMembership> groups) {
        this.groups = groups;
    }
}
