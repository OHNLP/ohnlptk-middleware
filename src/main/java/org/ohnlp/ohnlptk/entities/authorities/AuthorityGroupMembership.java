package org.ohnlp.ohnlptk.entities.authorities;

import org.ohnlp.ohnlptk.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITY_GROUP_MEMBERSHIPS")
public class AuthorityGroupMembership {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private User principal;

    @ManyToOne
    @JoinColumn(name = "id")
    private AuthorityGroup group;

    @Column
    private boolean isAdmin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
