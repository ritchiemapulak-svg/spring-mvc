package com.spring.spring_rest.security;

import com.spring.spring_rest.service.CustomUserDetailsService;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security configuration. Defines the password encoder, the
 * authentication manager and the HTTP security rules.
 */
@EnableWebSecurity 
@Configuration 
public class SecurityConfig {

    private CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService){
        this.setUserDetailsService(userDetailsService);
    }

    /**
     * Provides the BCrypt encoder for hashing and checking passwords.
     *
     * @return the password encoder
     */
    @Bean 
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the authentication manager used by the login endpoint.
     *
     * @param authentication the Spring Security configuration
     * @return the authentication manager
     * @throws Exception if it cannot be created
     */
    @Bean 
    AuthenticationManager authenticationManager
        (AuthenticationConfiguration authentication) throws Exception {
            return authentication.getAuthenticationManager();
    }

    /**
     * Defines the security rules: /api/auth/** is public, all other
     * requests need HTTP Basic authentication, and CSRF is disabled.
     *
     * @param security the HTTP security builder
     * @return the security filter chain
     * @throws Exception if the configuration fails
     */
    @Bean 
    SecurityFilterChain filterChain(HttpSecurity security)
        throws Exception {
            return security
                .cors(cors -> cors.configure(security))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth 
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/api/students/**")
                    .hasAnyAuthority("USER", "ADMIN")
                    .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();   
    }

    /**
     * Defines the CORS policy: allowed origins, HTTP methods and headers
     * for cross-origin requests, with credentials enabled. The policy
     * applies to every path.
     *
     * @return the CORS configuration source
     */
    @Bean 
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://172.30.240.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", 
            "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", 
            "Cache-Control", "Content-Type"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public void setUserDetailsService(
        CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public CustomUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

}
