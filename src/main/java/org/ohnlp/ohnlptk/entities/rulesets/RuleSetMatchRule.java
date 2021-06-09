package org.ohnlp.ohnlptk.entities.rulesets;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RULESET_MATCHRULES")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
public class RuleSetMatchRule {
    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String regexp;

    @Column
    private String rule_name;

    @Column
    private String location;

    @Column
    private String norm;

    @ManyToOne
    @JoinColumn
    private RuleSetDefinition definition;

    public Long getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSetMatchRule that = (RuleSetMatchRule) o;
        return Objects.equals(id, that.id) && Objects.equals(regexp, that.regexp) && Objects.equals(rule_name, that.rule_name) && Objects.equals(location, that.location) && Objects.equals(norm, that.norm) && Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regexp, rule_name, location, norm, definition);
    }
}
