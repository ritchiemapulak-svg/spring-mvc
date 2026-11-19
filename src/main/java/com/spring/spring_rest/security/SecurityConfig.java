// package com.spring.spring_rest.security;

// import com.spring.spring_rest.service.CustomUserDetailsService;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// /**
//  * Spring Security configuration. Defines the password encoder, the
//  * authentication manager and the HTTP security rules.
//  */
// @EnableWebSecurity 
// @Configuration 
// public class SecurityConfig {


//     private CustomUserDetailsService userDetailsService;

//     public SecurityConfig(CustomUserDetailsService userDetailsService){
//         this.setUserDetailsService(userDetailsService);
//     }

//     public void setUserDetailsService(
//         CustomUserDetailsService userDetailsService) {
//         this.userDetailsService = userDetailsService;
//     }

//     public CustomUserDetailsService getUserDetailsService() {
//         return userDetailsService;
//     }

//     /**
//      * Provides the BCrypt encoder for hashing and checking passwords.
//      *
//      * @return the password encoder
//      */
//     @Bean 
//     BCryptPasswordEncoder passwordEncoder(){
//         return new BCryptPasswordEncoder();
//     }

//     /**
//      * Exposes the authentication manager used by the login endpoint.
//      *
//      * @param authentication the Spring Security configuration
//      * @return the authentication manager
//      * @throws Exception if it cannot be created
//      */
//     @Bean 
//     AuthenticationManager authenticationManager
//         (AuthenticationConfiguration authentication) throws Exception {
//             return authentication.getAuthenticationManager();
//     }

//     /**
//      * Defines the security rules: /api/auth/** is public, all other
//      * requests need HTTP Basic authentication, and CSRF is disabled.
//      *
//      * @param security the HTTP security builder
//      * @return the security filter chain
//      * @throws Exception if the configuration fails
//      */
//     @Bean 
//     SecurityFilterChain filterChain(HttpSecurity security)
//         throws Exception {
//             return security
//                     .csrf(AbstractHttpConfigurer::disable)
//                     .authorizeHttpRequests(auth -> auth 
//                         .requestMatchers("/api/auth/**").permitAll()
//                         .anyRequest().authenticated()
//                     )
//                     .httpBasic(Customizer.withDefaults())
//                     .build();   
//     }

// }
