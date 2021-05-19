package org.ohnlp.ohnlptk.controllers;

import io.swagger.annotations.ApiOperation;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroupMembership;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.ohnlp.ohnlptk.repositories.RuleSetRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.ohnlp.ohnlptk.auth.AuthUtils.getUserForSpringSecurityContextAuth;

/**
 * Contains NLP ruleset management functions
 */
@Controller
@RequestMapping("/_ruleset")
public class RulesetController {
    private final RuleSetRepository ruleSetRepository;
    private final UserRepository userRepository;

    public RulesetController(RuleSetRepository ruleSetRepository, UserRepository userRepository) {
        this.ruleSetRepository = ruleSetRepository;
        this.userRepository = userRepository;
    }


    @ApiOperation("Retrieves a list of ruleset definitions for which the authenticated user has read access")
    @GetMapping("/getAllForUser")
    public List<RuleSetDefinition> getRulesets(Authentication auth) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        return this.ruleSetRepository.getRulesetsForUser(u);
    }

    @ApiOperation("Retrieves, if user has read permissions to it, the ruleset corresponding to ruleset_id")
    @GetMapping("/getForID")
    public ResponseEntity<RuleSetDefinition> getRulesetForID(Authentication auth, @RequestParam("ruleset_id") String rulesetID) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        RuleSetDefinition def = this.ruleSetRepository.getRuleSetDefinitionByRulesetId(rulesetID);
        if (def == null) {
            return ResponseEntity.notFound().build();
        }
        // Filter grants to semantic has read perms, then get group members and check if user is contained within
        if (def.getGrants().stream()
                .filter(g -> g.isRead() || g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .collect(Collectors.toSet()).contains(u)) {
            return ResponseEntity.ok(def);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Writes, if user has write permissions to it, the passed ruleset that should already exist. Note that" +
            " only NLP related changes are written and no changes to authority grants can be done through this endpoint")
    @PostMapping("/updateRuleset")
    public ResponseEntity<RuleSetDefinition> updateRuleset(Authentication auth, @RequestBody RuleSetDefinition def) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        RuleSetDefinition localDef = this.ruleSetRepository.getRuleSetDefinitionByRulesetId(def.getRulesetId());
        if (localDef == null) {
            return ResponseEntity.notFound().build();
        }
        if (localDef.getGrants().stream()
                .filter(g -> g.isWrite() || g.isManage())
                .map(AuthorityGrant::getPrincipal)
                .flatMap(group -> group.getMembers().stream())
                .map(AuthorityGroupMembership::getPrincipal)
                .map(User::getEmail)
                .collect(Collectors.toSet()).contains(u.getEmail())) {
            localDef.setName(def.getName());
            localDef.setRegexps(def.getRegexps());
            localDef.setMatchrules(def.getMatchrules());
            localDef.setContexts(def.getContexts());
            this.ruleSetRepository.save(localDef);
            return ResponseEntity.ok(localDef);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
