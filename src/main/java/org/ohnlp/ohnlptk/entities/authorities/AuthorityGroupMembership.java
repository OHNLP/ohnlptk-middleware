package org.ohnlp.ohnlptk.entities.authorities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "AUTHORITY_GROUP_MEMBERSHIP_DEFINITIONS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
public class AuthorityGroupMembership {
    @Id
    @Column(name = "MEMBERSHIP_DEF_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private User principal;

    @ManyToOne
    @JoinColumn
    private AuthorityGroup group;

    @Column
    private boolean isAdmin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPrincipal() {
        return principal;
    }

    public void setPrincipal(User principal) {
        this.principal = principal;
    }

    public AuthorityGroup getGroup() {
        return group;
    }

    public void setGroup(AuthorityGroup group) {
        this.group = group;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityGroupMembership that = (AuthorityGroupMembership) o;
        return isAdmin == that.isAdmin && Objects.equals(id, that.id) && Objects.equals(principal, that.principal) && Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, principal, group, isAdmin);
    }
}
