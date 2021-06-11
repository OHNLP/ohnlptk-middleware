package org.ohnlp.ohnlptk.dto.ruleset;

import org.ohnlp.ohnlptk.dto.DTOFactory;
import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;

public class RuleSetReferenceDTO extends LoadableDTO<RuleSetDefinition, RuleSetReferenceDTO> {

    private Long id;
    private String rulesetId;
    private String name;

    public RuleSetReferenceDTO() {
        super(RuleSetDefinition.class);
    }

    @Override
    public RuleSetReferenceDTO generateFromEntity(RuleSetDefinition entity) {
        this.id = entity.getId();
        this.rulesetId = entity.getRulesetId();
        this.name = entity.getName();
        return this;
    }

    @Override
    public RuleSetDefinition mergeFromDTO(RuleSetDefinition existing, DTOFactory factory) {
        return existing; // References cannot be edited
    }

    @Override
    public Object identityValue() {
        return id;
    }
}
