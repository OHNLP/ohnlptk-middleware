package org.ohnlp.ohnlptk.entities.authorities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "AUTHORITY_GROUPS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
public class AuthorityGroup {
    @Id
    @Column(name="GROUP_ID")
    @GeneratedValue
    private Long id;

    @Column
    @NaturalId
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private Collection<AuthorityGrant> grants;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "AUTHORITY_GROUP_MEMBERSHIPS",
            joinColumns = {
                    @JoinColumn(name = "GROUP_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "MEMBERSHIP_DEF_ID")
            }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<AuthorityGroupMembership> members;

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

    public Collection<AuthorityGrant> getGrants() {
        return grants;
    }

    public void setGrants(Collection<AuthorityGrant> grants) {
        this.grants = grants;
    }

    public Collection<AuthorityGroupMembership> getMembers() {
        return members;
    }

    public void setMembers(Collection<AuthorityGroupMembership> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityGroup that = (AuthorityGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
