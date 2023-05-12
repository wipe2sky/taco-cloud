package com.sia.tacos.configuration;

import com.sia.tacos.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .mvcMatchers("/design", "/orders").hasRole("USER")
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/login")

                .and()
                .oauth2Login()
                .loginPage("/login")

                .and()
                .logout()
                .logoutSuccessUrl("/")

                // Make H2-Console non-secured; for debug purposes
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .build();
    }
}
