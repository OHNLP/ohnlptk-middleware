package org.ohnlp.ohnlptk.dto.ruleset;

import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetMatchRule;

/**
 * DTO Representation of {@link RuleSetMatchRule}
 */
public class RuleSetMatchRuleDTO extends LoadableDTO<RuleSetMatchRule, RuleSetMatchRuleDTO> {

    private Long id;
    private String regexp;
    private String rule_name;
    private String location;
    private String norm;

    public RuleSetMatchRuleDTO() {
        super(RuleSetMatchRule.class);
    }

    @Override
    public RuleSetMatchRuleDTO generateFromEntity(RuleSetMatchRule entity) {
        this.id = entity.getId();
        this.regexp = entity.getRegexp();
        this.rule_name = entity.getRule_name();
        this.location = entity.getLocation();
        this.norm = entity.getNorm();
        return this;
    }

    @Override
    protected RuleSetMatchRule mergeFromDTO(RuleSetMatchRule existing, RuleSetMatchRuleDTO dto) {
        // ID is read-only
        existing.setRegexp(this.regexp);
        existing.setRule_name(this.rule_name);
        existing.setLocation(this.location);
        existing.setNorm(this.norm);
        return existing;
    }

    @Override
    public Object identityValue() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNorm() {
        return norm;
    }

    public void setNorm(String norm) {
        this.norm = norm;
    }
}
