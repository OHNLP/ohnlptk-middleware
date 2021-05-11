package org.ohnlp.ohnlptk.entities.rulesets;

import javax.persistence.*;

@Entity
@Table(name = "RULESET_MATCHRULES")
public class RuleSetMatchRule {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String regexp;

    @Column
    private String rule_name;

    @Column
    private String location;

    @Column
    private String norm;

    @ManyToOne
    @JoinColumn(name = "id")
    private RuleSetDefinition definition;



    public long getId() {
        return id;
    }

    public String getRegexp() {
        return regexp;
    }

    public String getRule_name() {
        return rule_name;
    }

    public String getLocation() {
        return location;
    }

    public String getNorm() {
        return norm;
    }
}
