package com.example.wigelltravels_.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService uds() {
        var admin = User.withUsername("Courtney")
                .password("{noop}Love")
                .roles("ADMIN","USER")
                .build();

        var user1 = User.withUsername("Kurt")
                .password("{noop}Cobain")
                .roles("USER")
                .build();

        var user2 = User.withUsername("Dave")
                .password("{noop}Grohl")
                .roles("USER")
                .build();

        var user3 = User.withUsername("Krist")
                .password("{noop}Novoselic")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
