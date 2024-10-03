package com.example.wallet.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//    private JwtRequestFilter jwtRequestFilter;
//
//    // Configure SecurityFilterChain instead of extending WebSecurityConfigurerAdapter
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Disable CSRF since we are using JWTs
//                .csrf(csrf -> csrf.disable())
//
//                // Authorize requests: allowing public access to signup, securing other routes
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/person/signup").permitAll() // Allow public access to signup
//                        .anyRequest().authenticated() // Secure all other routes
//                )
//
//                // Stateless session management, as we're using JWT
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        // Add the JWT filter before UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // Provide AuthenticationManager bean, needed for authentication
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/person/signup").permitAll()  // Allow access to /signup without authentication
                        .anyRequest().authenticated()  // Require authentication for all other routes
                )
                .csrf(csrf -> csrf.disable());  // Disable CSRF (if you're not using sessions)

        return http.build();
    }


}
