package com.lms.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // AUTH
                .requestMatchers("/auth/**").permitAll()

                // ADMIN
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                // STUDENT
                .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")

                // INSTRUCTOR
                .requestMatchers("/instructor/**").hasAuthority("ROLE_INSTRUCTOR")

                // PARENT
                .requestMatchers("/parent/**").hasAuthority("ROLE_PARENT")

                // PROFILE
                .requestMatchers("/me/**").authenticated()

                .anyRequest().authenticated()
            )
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
