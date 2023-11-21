package com.stg.b2b.config;

import com.stg.b2b.exception.ForbiddenException;
import com.stg.b2b.security.UserDetailsService;
import com.stg.b2b.security.jwt.JwtAuthenticationEntryPoint;
import com.stg.b2b.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  Defining the security rules and mechanisms that control access
 *  to various resources and endpoints within the application
 *
 */
@Configuration
public class WebSecurityConfig {
    private static final String[] FILTER_UNSECURED_URL = {"/mail/**","/auth/*", "/swagger-ui/**","/graphiql/**","/graphql/**", "/v3/api-docs", "/v3/api-docs/swagger-config", "/swagger-ui.html"};


    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



    @Value("${stg.login.method}")
    private String loginMethod;

    @Value("${stg.ldap.url}")
    private String ldapUrl;

    @Value("${stg.ldap.user.search.filter}")
    private String userSearchFilter;


    @Value("${stg.ldap.manager.dn}")
    private String managerDn;

    @Value("${stg.ldap.manager.password}")
    private String managerPassword;


    /**
     * Customizing the security behavior of the application by specifying the order
     * and composition of the security filters for different URL patterns.
     *
     * @param httpSecurity
     * @return httpSecurity
     * @throws Exception
     *
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests(requests -> {
            try {
                requests.requestMatchers(FILTER_UNSECURED_URL).permitAll().anyRequest()
                    .authenticated().and().sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            } catch (Exception e) {
                throw new ForbiddenException(e.getMessage());
            }
        });

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    /**
     * In this method, we are customizing how authentication is performed in the application
     * Since we are doing LDAP based authentication,
     * here we are setting LDAP properties to the authenticationManagerBuilder
     * so that it can establish the connection with the required LDAP
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.ldapAuthentication()
                .userSearchFilter(userSearchFilter)
                .contextSource().url(ldapUrl)
                .managerDn(managerDn)
                .managerPassword(managerPassword);

    }

    /**
     *  Here we ensure that user credentials are verified correctly.
     *  This method centralizes and standardizes the authentication logic in the application,
     *  providing a consistent way to authenticate users
     *  regardless of the authentication mechanism being used.
     *
     * @param authenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * We are configuring CORS using WebMvcConfigurer,
     * With this we can define the allowed origins, methods, headers,
     * and other CORS-related settings for specific URL patterns
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "HEAD","DELETE");
            }
        };
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }
}
