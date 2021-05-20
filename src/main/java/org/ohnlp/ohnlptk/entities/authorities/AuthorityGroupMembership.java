package org.ohnlp.ohnlptk.entities.authorities;

import org.ohnlp.ohnlptk.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITY_GROUP_MEMBERSHIPS")
public class AuthorityGroupMembership {
    @Id
    @Column
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
}
