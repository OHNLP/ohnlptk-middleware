package org.ohnlp.ohnlptk.entities.rulesets;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.ohnlp.ohnlptk.entities.resolvers.JPAEntityResolver;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RULESET_REGEXPS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = JPAEntityResolver.class)
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

    public RuleSetDefinition getDefinition() {
        return definition;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDefinition(RuleSetDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSetRegularExpression that = (RuleSetRegularExpression) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(text, that.text) && Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, definition);
    }
}
