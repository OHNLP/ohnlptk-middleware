package org.ohnlp.ohnlptk.entities.authorities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "AUTHORITY_GRANTS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
public class AuthorityGrant {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private RuleSetDefinition ruleset;

    @ManyToOne
    @JoinColumn
    private AuthorityGroup principal;

    @Column
    private boolean read;

    @Column
    private boolean write;

    @Column
    private boolean manage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuleSetDefinition getRuleset() {
        return ruleset;
    }

    public void setRuleset(RuleSetDefinition ruleset) {
        this.ruleset = ruleset;
    }

    public AuthorityGroup getPrincipal() {
        return principal;
    }

    public void setPrincipal(AuthorityGroup principal) {
        this.principal = principal;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityGrant that = (AuthorityGrant) o;
        return read == that.read && write == that.write && manage == that.manage && Objects.equals(id, that.id) && Objects.equals(ruleset, that.ruleset) && Objects.equals(principal, that.principal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleset, principal, read, write, manage);
    }
}
