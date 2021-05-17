package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleSetRepository extends JpaRepository<RuleSetDefinition, Long> {
    RuleSetDefinition getRuleSetDefinitionByRulesetId(String projectID);

}
