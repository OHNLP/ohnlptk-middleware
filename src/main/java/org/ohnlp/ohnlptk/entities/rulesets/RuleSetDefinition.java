package org.ohnlp.ohnlptk.entities.rulesets;

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
    private String project_id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "id")
    private Collection<RuleSetRegularExpression> regexps;

    @OneToMany
    @JoinColumn(name = "id")
    private Collection<RuleSetMatchRule> matchrules;

    @Column
    private String contexts;

    public long getId() {
        return id;
    }

    public String getProject_id() {
        return project_id;
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
}
