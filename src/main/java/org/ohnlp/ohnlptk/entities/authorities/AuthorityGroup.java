package org.ohnlp.ohnlptk.entities.authorities;

import org.ohnlp.ohnlptk.entities.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "AUTHORITY_GROUPS")
public class AuthorityGroup {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn
    private Collection<AuthorityGrant> grants;

    @ManyToMany
    @JoinColumn
    private Collection<AuthorityGroupMembership> members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
