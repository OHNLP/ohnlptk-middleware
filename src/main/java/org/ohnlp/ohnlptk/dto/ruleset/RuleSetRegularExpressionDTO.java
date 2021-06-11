package org.ohnlp.ohnlptk.dto.ruleset;

import org.ohnlp.ohnlptk.dto.LoadableDTO;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetRegularExpression;

public class RuleSetRegularExpressionDTO extends LoadableDTO<RuleSetRegularExpression, RuleSetRegularExpressionDTO> {
    private Long id;
    private String name;
    private String text;

    public RuleSetRegularExpressionDTO() {
        super(RuleSetRegularExpression.class);
    }

    @Override
    public RuleSetRegularExpressionDTO generateFromEntity(RuleSetRegularExpression entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.text = entity.getText();
        return this;
    }

    @Override
    protected RuleSetRegularExpression mergeFromDTO(RuleSetRegularExpression existing, RuleSetRegularExpressionDTO dto) {
        // ID Cannot be changed
        existing.setName(this.name);
        existing.setText(this.text);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
