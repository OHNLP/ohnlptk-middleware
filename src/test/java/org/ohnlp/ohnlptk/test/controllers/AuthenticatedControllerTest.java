package org.ohnlp.ohnlptk.test.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ohnlp.ohnlptk.OHNLPTKWebApplication;
import org.ohnlp.ohnlptk.auth.AuthAndAccessComponent;
import org.ohnlp.ohnlptk.auth.oidc.OAuth2UserRegistrationService;
import org.ohnlp.ohnlptk.controllers.RulesetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

/**
 * Abstract class setting up a mock user 1 and user 2 under {@link #mockUserAuth} and {@link #mockUserAuth2}
 * that can be used for any authenticated requests for controller tests.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OHNLPTKWebApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AuthenticatedControllerTest {
    @Autowired
    protected OAuth2UserRegistrationService oidcUserRegistrationService;

    @Autowired
    protected AuthAndAccessComponent authAndAccessComponent;

    // Shared test variables
    protected Authentication mockUserAuth;
    protected Authentication mockUserAuth2;


    @BeforeAll
    public void init() {
        // Set up mock user for testing purposes
        this.oidcUserRegistrationService.loadUserLocal("test@ohnlp.org", "OHNLP Test User", null);
        this.oidcUserRegistrationService.loadUserLocal("test2@ohnlp.org", "OHNLP Test User 2", null);

        this.mockUserAuth = new UsernamePasswordAuthenticationToken("test@ohnlp.org", null, Collections.emptyList());
        this.mockUserAuth2 = new UsernamePasswordAuthenticationToken("test2@ohnlp.org", null, Collections.emptyList());
    }
}
