package org.ohnlp.ohnlptk.auth;

import org.ohnlp.ohnlptk.auth.filters.APIKeyAuthorizationFilter;
import org.ohnlp.ohnlptk.auth.oidc.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

/**
 * Implements multi-method authentication by specifying separate security configuration
 * adapters using header matching. Currently supports, by order of priority:
 * <ul>
 *     <li>
 *         Principal/API-Key basic authentication
 *     </li>
 *     <li>
 *         Oauth2 (via Github, Google)
 *     </li>
 * </ul>
 *
 * NIH OneLogin (SAML) Support is Planned to Replace Oauth2
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {


    /**
     * Basic (Principal/API-Key) Auth Configuration
     */
    @Order(1)
    @Configuration
    public static class APIKeySecurityConfig extends WebSecurityConfigurerAdapter {
        private final APIKeyAuthorizationFilter apiKeyAuthorizationFilter;

        @Autowired
        public APIKeySecurityConfig(APIKeyAuthorizationFilter apiKeyAuthorizationFilter) {
            this.apiKeyAuthorizationFilter = apiKeyAuthorizationFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors()
                    .and()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                    .addFilterAfter(this.apiKeyAuthorizationFilter, BasicAuthenticationFilter.class);
        }

    }
    /**
     * Oauth2 Security Configuration
     */
    @Order(2)
    @Configuration
    public static class OauthSecurityConfig extends WebSecurityConfigurerAdapter {

        private final OAuth2UserService oauthUserService;

        public OauthSecurityConfig(OAuth2UserService oauthUserService) {
            this.oauthUserService = oauthUserService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/oauth2/registration/**", "/login", "/login/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login().userInfoEndpoint().userService(this.oauthUserService);
        }

    }
}
