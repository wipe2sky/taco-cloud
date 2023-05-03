package com.sia.tacos.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Storage user details in memory
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> usersList = new ArrayList<>();
        usersList.add(new User(
                "buzz", passwordEncoder.encode("password"),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))

        ));
        usersList.add(new User(
                "woody", passwordEncoder.encode("password"),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ));

        return new InMemoryUserDetailsManager(usersList);
    }
}
