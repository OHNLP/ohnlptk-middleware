package org.ohnlp.ohnlptk.entities.authorities;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITY_GRANTS")
public class AuthorityGrant {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
