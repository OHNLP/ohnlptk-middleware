package org.ohnlp.ohnlptk.test.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.ohnlp.ohnlptk.controllers.RulesetController;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetDefinition;
import org.ohnlp.ohnlptk.entities.rulesets.RuleSetRegularExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tests REST endpoints under the /_ruleset path.
 * Note that tests here are done using direct java calls to relevant functions and not by doing the REST calls themselves.
 * We do testing via REST calls elsewhere during full-stack integration testing.
 */
public class RulesetControllerUnitTests extends AuthenticatedControllerTest {

    @Autowired
    private RulesetController rulesetController;

    private String testRulesetId;
    private String otherUserTestRulesetId;

    /**
     * Tests the /newRuleset REST endpoint
     */
    @Test
    @Order(1)
    public void newRulesetTest() {
        RuleSetDefinition definition = this.rulesetController.createRuleset(this.mockUserAuth, "Test Ruleset").getBody();
        Assert.notNull(definition, "Returned definition after create is null");
        Assert.notNull(definition.getId(), "EntityManager is not autopopulating definition ID on create");
        Assert.notNull(definition.getRulesetId(), "A ruleset lookup UUID is not autopopulated on create");
        Assert.isTrue(definition.getName().equals("Test Ruleset"), "Created Name Mismatches Input Ruleset Name");
        Assert.isTrue(definition.getGrants().size() > 0, "Created ruleset has no permission grants assigned");
        Assert.isTrue(this.authAndAccessComponent.userCanManageRuleset(
                                this.authAndAccessComponent.getUserForSpringSecurityContextAuth(this.mockUserAuth),
                                definition),
                "Created RulesetDefinition does not have the Creating User with Manage Permissions!");
        this.testRulesetId = definition.getRulesetId();

    }

    /**
     * Populates ruleset repository with 5 additional randomly named rulesets now that we validate ruleset creation works
     *
     * Also populates ruleset with 1 additional randomly named ruleset under a different user
     */
    @Test
    @Order(2)
    public void rulesetRepositorySetup() {
        for (int i = 0; i < 5; i++) {
            this.rulesetController.createRuleset(this.mockUserAuth, UUID.randomUUID().toString());
        }
        this.otherUserTestRulesetId = this.rulesetController.createRuleset(this.mockUserAuth2, "Test Ruleset 2").getBody().getRulesetId();
    }

    /**
     * Tests the /getForID REST endpoint
     *
     * Verifies that the retrieved ruleset is the Test Ruleset previously created during {@link #newRulesetTest()}
     * Also verifies no result returned if user does not have rights to the ruleset in question
     */
    @Test
    @Order(3)
    public void getForIDTest() {
        // Check for created ruleset with correct user
        RuleSetDefinition def = this.rulesetController.getRulesetForID(this.mockUserAuth, this.testRulesetId).getBody();
        Assert.notNull(def, "Ruleset Repository does not contain the previously created ruleset definition");
        Assert.isTrue(def.getName().equals("Test Ruleset"), "Retrieved ruleset is of a different name");
        Assert.isTrue(def.getRulesetId().equals(this.testRulesetId), "Retrieved ruleset is of a ruleset ID");

        def = this.rulesetController.getRulesetForID(this.mockUserAuth2, this.otherUserTestRulesetId).getBody();
        Assert.notNull(def, "Ruleset Repository does not contain the previously created ruleset definition");
        Assert.isTrue(def.getName().equals("Test Ruleset 2"), "Retrieved ruleset is of a different name");
        Assert.isTrue(def.getRulesetId().equals(this.otherUserTestRulesetId), "Retrieved ruleset is of a ruleset ID");

        // Check retrieval of other user's created ruleset returns nothing with error code 404
        ResponseEntity<RuleSetDefinition> resp = this.rulesetController.getRulesetForID(this.mockUserAuth2, this.testRulesetId);
        Assert.isNull(resp.getBody(), "A ruleset was returned when user should not have read access");
        Assert.isTrue(resp.getStatusCodeValue() == 404,
                "An error code other than 404 not found returned for mis-authenticated retrievals, potentially" +
                        "leaking information!");

        // Check retrieval using bogus UUID
        resp = this.rulesetController.getRulesetForID(this.mockUserAuth2, UUID.randomUUID().toString());
        Assert.isNull(resp.getBody(), "A ruleset was returned when there should not be any");
        Assert.isTrue(resp.getStatusCodeValue() == 404,
                "An error code other than 404 not found returned for non-existing ruleset");
    }


    /**
     * Tests the /getAllForUser REST endpoint
     *
     * Verifies that authenticated ruleset retrieval returns 6 items (1 created in {@link #newRulesetTest()},
     * 5 from {@link #rulesetRepositorySetup()} and checks that the user has read access to all of the returned results
     */
    @Test
    @Order(4)
    public void getAllForUserTest() throws JsonProcessingException {
        Map<String, String> defs = this.rulesetController.getRulesets(this.mockUserAuth);
        Assert.isTrue( defs.size() == 6, "Expected 6 returned rulesets, got " + defs.size());
        Assert.isTrue(
                defs.containsValue(this.testRulesetId),
                "Created ruleset is not in the returned ruleset list");
        // Ensure user actually has read perms to all retrieved rulesets
        for (String rulesetID : defs.values()) {
            RuleSetDefinition definition = this.rulesetController.getRulesetForID(this.mockUserAuth, rulesetID).getBody();
            Assert.isTrue(
                    this.authAndAccessComponent.userCanReadRuleset(
                            this.authAndAccessComponent.getUserForSpringSecurityContextAuth(this.mockUserAuth),
                            definition),
                    "A ruleset for which the user does not have read access was returned!"
                            + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(definition));
        }
    }

    /**
     * Tests the /updateRuleset REST endpoint
     *
     * Adds a new regexp terms list and verifies that retrieving the ruleset again by ruleset ID reflects those changes.
     * Also verifies that modifications to other people's rulesets cannot be done
     *
     * TODO: verify read vs. write/manage access by creating new project and manually assigning only read perms
     * TODO: more thorough tests of individual changes/change permutations to the input ruleset to be updated
     */
    @Test
    @Order(5)
    public void updateRulesetTest() {
        // We already test this works correctly in #getForIDTest()
        RuleSetDefinition def = this.rulesetController.getRulesetForID(this.mockUserAuth, this.testRulesetId).getBody();
        // Verify not polluted from elsewhere
        Assert.isTrue(Objects.requireNonNull(def).getRegexps().isEmpty(), "Regular Expressions are Not Empty Before Update");
        // Mock a sample regex and update our definition with it
        RuleSetRegularExpression regex = Mockito.mock(RuleSetRegularExpression.class);
        Mockito.when(regex.getName()).thenReturn("TESTREGEXLIST");
        Mockito.when(regex.getText()).thenReturn("Test Regex 1\nTest Regex 2\r\nTest Regex 3");
        Objects.requireNonNull(def).setRegexps(new ArrayList<>(Collections.singletonList(regex)));
        def = this.rulesetController.updateRuleset(this.mockUserAuth, def).getBody();
        Assert.notNull(def, "Returned RulesetDefinition after Update is Null");
        Assert.isTrue(!def.getRegexps().isEmpty(), "Returned Regular Expressions are Empty after Update");
        Map<String, String> defs = this.rulesetController.getRulesets(this.mockUserAuth);
        Assert.isTrue( defs.size() == 6, "Expected 6 returned rulesets, got " + defs.size() + ", " +
                "merge did not occur correctly!");

        // Now try doing the same thing, except we don't have write permissions - retrieve mockUserAuth2's project here
        def = this.rulesetController.getRulesetForID(this.mockUserAuth2, this.otherUserTestRulesetId).getBody();
        // Verify not polluted from elsewhere
        Assert.isTrue(Objects.requireNonNull(def).getRegexps().isEmpty(), "Regular Expressions are Not Empty Before Update");
        Objects.requireNonNull(def).setRegexps(Collections.singletonList(regex));
        // And save using mockUserAuth instead
        ResponseEntity<RuleSetDefinition> resp = this.rulesetController.updateRuleset(this.mockUserAuth, def);
        Assert.isNull(resp.getBody(), "A ruleset was returned when user should not have write access");
        Assert.isTrue(resp.getStatusCodeValue() == 404,
                "An error code other than 404 not found returned for mis-authenticated writes, potentially" +
                        "leaking information!");
        defs = this.rulesetController.getRulesets(this.mockUserAuth2);
        Assert.isTrue( defs.size() == 1, "Expected 1 returned ruleset, got " + defs.size() + ", " +
                "writing occurred despite no permissions!");

    }

    /**
     * Tests the /deleteRuleset endpoint
     *
     * Verifies that deletion occurs, and that deleting projects for which you do not have manage access does NOT work
     */
    @Test
    @Order(6)
    public void deleteRulesetTest() {
        // Delete mockUserAuth2's ruleset and verify
        Map<String, String> defs = this.rulesetController.deleteRuleset(this.mockUserAuth2, this.otherUserTestRulesetId);
        Assert.isTrue( defs.size() == 0, "Expected 0 returned rulesets, got " + defs.size() + ", " +
                "delete did not occur correctly!");

        // Now delete mockUserAuth's ruleset using mockUserAuth2 and verify no delete occurred
        this.rulesetController.deleteRuleset(this.mockUserAuth2, this.testRulesetId);
        int size = this.rulesetController.getRulesets(this.mockUserAuth).size();
        Assert.isTrue(size == 6,
                "Expected 6 returned rulesets, got " + size + ", delete occurred despite no permissions");
    }

}
