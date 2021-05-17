package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RuleSetRepository extends JpaRepository<RuleSetDefinition, Long> {
    RuleSetDefinition getRuleSetDefinitionByRulesetId(String projectID);

    @Query("SELECT DISTINCT r FROM RuleSetDefinition r " +
            "INNER JOIN r.grants grant " +
            "INNER JOIN grant.principal authgroup " +
            "INNER JOIN authgroup.members members " +
            "WHERE members.principal = ?1 " +
            " and (grant.read = true " +
            "  or grant.write = true  " +
            "  or grant.manage = true)")
    List<RuleSetDefinition> getRulesetsForUser(User u);
}
