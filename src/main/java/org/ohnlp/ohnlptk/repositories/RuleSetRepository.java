package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleSetRepository extends JpaRepository<RuleSetDefinition, Long> {
    public RuleSetDefinition getRuleSetDefinitionByProject_id(String projectID);
}
