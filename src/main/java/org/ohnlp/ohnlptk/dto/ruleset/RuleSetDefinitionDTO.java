package org.ohnlp.ohnlptk.dto.ruleset;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.dto.authorities.RuleSetViewAuthorityGrantDTO;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * DTO Representation of a {@link org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition}
 */
public class RuleSetDefinitionDTO extends LoadableDTO<RuleSetDefinition, RuleSetDefinitionDTO> {
    private Long id;
    private String rulesetId;
    private String name;
    private Collection<RuleSetRegularExpressionDTO> regexps;
    private Collection<RuleSetMatchRuleDTO> matchRules;
    private String contexts;
    private Collection<RuleSetViewAuthorityGrantDTO> grants;

    public RuleSetDefinitionDTO() {
        super(RuleSetDefinition.class);
    }

    @Override
    public RuleSetDefinitionDTO generateFromEntity(RuleSetDefinition entity) {
        this.id = entity.getId();
        this.rulesetId = entity.getRulesetId();
        this.name = entity.getName();
        this.regexps = entity.getRegexps().stream()
                .map(r -> new RuleSetRegularExpressionDTO().generateFromEntity(r))
                .collect(Collectors.toList());
        this.matchRules = entity.getMatchrules().stream()
                .map(r -> new RuleSetMatchRuleDTO().generateFromEntity(r))
                .collect(Collectors.toList());
        this.contexts = entity.getContexts();
        this.grants = entity.getGrants().stream()
                .map(r -> new RuleSetViewAuthorityGrantDTO().generateFromEntity(r))
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public RuleSetDefinition mergeFromDTO(RuleSetDefinition existing, DTOFactory factory) {
        // id and rulesetId are read-only
        existing.setName(this.name);
        // TODO when merging handle potential deletions by cross-checking IDs and deleting any entities from db that are
        // removed - instead we are currently leaving orphaned records everywhere in DB
        existing.setRegexps(this.regexps.stream().map(factory::mergeOrCreate)
                .peek(r -> r.setDefinition(existing))
                .collect(Collectors.toList()));
        existing.setMatchrules(this.matchRules.stream().map(factory::mergeOrCreate)
                .peek(r -> r.setDefinition(existing))
                .collect(Collectors.toList()));
        existing.setContexts(this.contexts);
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

    public String getRulesetId() {
        return rulesetId;
    }

    public void setRulesetId(String rulesetId) {
        this.rulesetId = rulesetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<RuleSetRegularExpressionDTO> getRegexps() {
        return regexps;
    }

    public void setRegexps(Collection<RuleSetRegularExpressionDTO> regexps) {
        this.regexps = regexps;
    }

    public Collection<RuleSetMatchRuleDTO> getMatchRules() {
        return matchRules;
    }

    public void setMatchRules(Collection<RuleSetMatchRuleDTO> matchRules) {
        this.matchRules = matchRules;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public Collection<RuleSetViewAuthorityGrantDTO> getGrants() {
        return grants;
    }

    public void setGrants(Collection<RuleSetViewAuthorityGrantDTO> grants) {
        this.grants = grants;
    }
}
