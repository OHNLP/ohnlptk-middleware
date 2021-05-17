package org.ohnlp.ohnlptk.entities.rulesets;

import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "RULESET_DEFS")
public class RuleSetDefinition {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String rulesetId;

    @Column
    private String name;

    @OneToMany
    @JoinColumn
    private Collection<RuleSetRegularExpression> regexps;

    @OneToMany
    @JoinColumn
    private Collection<RuleSetMatchRule> matchrules;

    @Column
    private String contexts;

    @OneToMany
    @JoinColumn
    private Collection<AuthorityGrant> grants;

    public long getId() {
        return id;
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public String getName() {
        return name;
    }

    public Collection<RuleSetRegularExpression> getRegexps() {
        return regexps;
    }

    public Collection<RuleSetMatchRule> getMatchrules() {
        return matchrules;
    }

    public String getContexts() {
        return contexts;
    }

    public Collection<AuthorityGrant> getGrants() {
        return grants;
    }
}
