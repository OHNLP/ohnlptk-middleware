package org.ohnlp.ohnlptk.entities.rulesets;

import javax.persistence.*;

@Entity
@Table(name = "RULESET_REGEXPS")
public class RuleSetRegularExpression {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn
    private RuleSetDefinition definition;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
