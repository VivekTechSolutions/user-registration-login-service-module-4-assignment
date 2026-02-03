package com.example.User_Registration_Login_Service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for Postman/testing
            .authorizeHttpRequests(auth -> auth
                // Allow register and login endpoints without authentication
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.disable())  // disable basic auth
            .formLogin(form -> form.disable());           // disable default form login

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // used to encrypt passwords
    }
}
