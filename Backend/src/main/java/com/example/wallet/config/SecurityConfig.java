package com.example.wallet.config;
import com.example.wallet.security.JwtAuthenticationEntryPoint;
import com.example.wallet.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint entryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.entryPoint = entryPoint;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/person/signup").permitAll()  // Allow access to /signup without authentication
                        .anyRequest().authenticated()  // Require authentication for all other routes

                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .csrf(AbstractHttpConfigurer::disable);  // Disable CSRF (if you're not using sessions)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
