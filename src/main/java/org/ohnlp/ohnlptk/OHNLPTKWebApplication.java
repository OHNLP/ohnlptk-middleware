package org.ohnlp.ohnlptk;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.ohnlp.ohnlptk.auth.filters.APIKeyAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableEncryptableProperties
public class OHNLPTKWebApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OHNLPTKWebApplication.class);
        application.run(args);
    }


    /**
     * Enable CORS for all endpoints.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods("GET", "POST", "HEAD", "DELETE", "PUT")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // Disable autoenabling api-key authorization for everything
    @Bean
    public FilterRegistrationBean<APIKeyAuthorizationFilter> registration(APIKeyAuthorizationFilter filter) {
        FilterRegistrationBean<APIKeyAuthorizationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
