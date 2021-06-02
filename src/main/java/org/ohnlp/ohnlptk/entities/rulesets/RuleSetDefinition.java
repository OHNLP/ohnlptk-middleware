package org.ohnlp.ohnlptk.entities.rulesets;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "RULESET_DEFS")
public class RuleSetDefinition {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    @NaturalId
    private String rulesetId;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<RuleSetRegularExpression> regexps;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<RuleSetMatchRule> matchrules;

    @Column
    private String contexts;

    // Eager-load here because we will always be checking authorities on retrieval anyways
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<AuthorityGrant> grants;

    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setRulesetId(String rulesetId) {
        this.rulesetId = rulesetId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegexps(Collection<RuleSetRegularExpression> regexps) {
        this.regexps = regexps;
    }

    public void setMatchrules(Collection<RuleSetMatchRule> matchrules) {
        this.matchrules = matchrules;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public void setGrants(Collection<AuthorityGrant> grants) {
        this.grants = grants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSetDefinition that = (RuleSetDefinition) o;
        return Objects.equals(id, that.id) && Objects.equals(rulesetId, that.rulesetId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rulesetId, name);
    }
}
