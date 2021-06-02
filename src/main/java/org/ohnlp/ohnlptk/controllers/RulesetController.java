package org.ohnlp.ohnlptk.controllers;

import io.swagger.annotations.ApiOperation;
import org.ohnlp.ohnlptk.auth.AuthUtils;
import org.ohnlp.ohnlptk.entities.User;
import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.ohnlp.ohnlptk.repositories.AuthorityGroupRepository;
import org.ohnlp.ohnlptk.repositories.RuleSetRepository;
import org.ohnlp.ohnlptk.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.ohnlp.ohnlptk.auth.AuthUtils.*;

/**
 * Contains NLP ruleset management functions
 */
@Controller
@RequestMapping("/_ruleset")
public class RulesetController {
    private final RuleSetRepository ruleSetRepository;
    private final UserRepository userRepository;
    private final AuthorityGroupRepository authorityGroupRepository;

    public RulesetController(RuleSetRepository ruleSetRepository, UserRepository userRepository,
                             AuthorityGroupRepository authorityGroupRepository) {
        this.ruleSetRepository = ruleSetRepository;
        this.userRepository = userRepository;
        this.authorityGroupRepository = authorityGroupRepository;
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
        if (AuthUtils.userCanReadRuleset(u, def)) {
            return ResponseEntity.ok(def);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Creates and returns new ruleset with the given name, with the requesting user as only person with " +
            "manage access")
    @PostMapping("/newRuleset")
    public ResponseEntity<RuleSetDefinition> createRuleset(Authentication auth, @RequestParam("name") String rulesetName) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        // Create new ruleset itself
        RuleSetDefinition def = new RuleSetDefinition();
        def.setName(rulesetName);
        def.setRulesetId(UUID.randomUUID().toString().toUpperCase(Locale.ROOT));
        // Populate new ruleset with authority grant for requesting user
        AuthorityGrant grant = new AuthorityGrant();
        grant.setManage(true);
        grant.setWrite(true);
        grant.setRead(true);
        grant.setRuleset(def);
        grant.setPrincipal(this.authorityGroupRepository
                .getAuthorityGroupByName(u.getEmail().toLowerCase(Locale.ROOT)));
        def.setGrants(Collections.singleton(grant));
        def = this.ruleSetRepository.save(def);
        return ResponseEntity.ok(def);
    }

    @ApiOperation("Writes, if user has write permissions to it, the passed ruleset that should already exist")
    @PostMapping("/updateRuleset")
    public ResponseEntity<RuleSetDefinition> updateRuleset(Authentication auth, @RequestBody RuleSetDefinition def) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        RuleSetDefinition localDef = this.ruleSetRepository.getRuleSetDefinitionByRulesetId(def.getRulesetId());
        if (localDef == null) {
            return ResponseEntity.notFound().build();
        }
        if (userCanWriteRuleset(u, localDef)) {
            def = this.ruleSetRepository.save(def);
            return ResponseEntity.ok(def);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Deletes, if user has manage permissions to it, the ruleset associated with the passed ruleset ID. " +
            "Returns the list of the authenticating user's projects")
    @DeleteMapping("/deleteRuleset")
    public List<RuleSetDefinition> deleteRuleset(Authentication auth, @RequestParam("ruleset_id") String rulesetId) {
        User u = getUserForSpringSecurityContextAuth(auth, this.userRepository);
        RuleSetDefinition localDef = this.ruleSetRepository.getRuleSetDefinitionByRulesetId(rulesetId);
        if (localDef != null) {
            if (userCanManageRuleset(u, localDef)) {
                this.ruleSetRepository.delete(localDef);
            }
        }
        return getRulesets(auth);
    }
}
