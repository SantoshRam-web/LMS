package com.lms.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

            	    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
            	    
            	    // 2️⃣ PLATFORM IMPORT (EXPLICIT METHOD FIRST)
                    .requestMatchers(
                            org.springframework.http.HttpMethod.POST,
                            "/platform/themes/import"
                    ).permitAll()
            	    
            	    // 3️⃣ ALL PLATFORM APIs
                    .requestMatchers("/platform/**").permitAll()
                    
                     // 4️⃣ PUBLIC AUTH APIs
                    .requestMatchers("/auth/**").permitAll()
            	    // 🔓 PUBLIC
            	    .requestMatchers("/auth/login").permitAll()
            	    .requestMatchers("/auth/logout").permitAll()
            	    .requestMatchers("/auth/password-reset/**").permitAll()
            	    .requestMatchers("/auth/account-unlock/**").permitAll()
            	    .requestMatchers("/platform/**").permitAll()

            	    // 🔓 SUPER ADMIN SIGNUP (ONLY OTP FLOW)
            	    .requestMatchers("/super-admin/signup/**").permitAll()
            	    .requestMatchers("/super-admin/request-disable").permitAll()

            	    // 🔐 SUPER ADMIN (ALL OTHER SUPER ADMIN APIs)
            	    .requestMatchers("/super-admin/**").hasAuthority("ROLE_SUPER_ADMIN")

            	    // 🔐 ADMIN
            	    .requestMatchers("/admin/**")
            	    .hasAnyAuthority("ROLE_ADMIN","ROLE_SUPER_ADMIN")

            	    // 🔐 STUDENT
            	    .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")

            	    // 🔐 INSTRUCTOR
            	    .requestMatchers("/instructor/**").hasAuthority("ROLE_INSTRUCTOR")

            	    // 🔐 PARENT
            	    .requestMatchers("/parent/**").hasAuthority("ROLE_PARENT")
            	    
            	     // 🔐 PARENT
            	    .requestMatchers("/driver/**").hasAuthority("ROLE_DRIVER")
            	    
            	     // 🔐 PARENT
            	    .requestMatchers("/conductor/**").hasAuthority("ROLE_CONDUCTOR")

            	    // 🔐 SELF
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
    org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration =
                new org.springframework.web.cors.CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
