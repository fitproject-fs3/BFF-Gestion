package com.fitproject.bff.config;

import com.fitproject.bff.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                // Worker-specific endpoints (must be before the general evidence rule)
                .requestMatchers("/api/v1/evidence/worker/**").hasAnyRole("ADMIN", "SUPERVISOR_OBRA", "TRABAJADOR")
                .requestMatchers("/api/v1/evidence/*/worker-submit").hasRole("TRABAJADOR")
                .requestMatchers("/api/v1/projects").hasAnyRole("ADMIN", "SUPERVISOR_OBRA", "TRABAJADOR")
                // Admin-only: project creation goes through the mobile controller
                .requestMatchers(HttpMethod.POST, "/api/v1/mobile/projects").hasRole("ADMIN")
                // Inventory: workers can read (to select insumos); admin can create/update
                .requestMatchers(HttpMethod.GET, "/api/v1/inventory/insumos").hasAnyRole("ADMIN", "SUPERVISOR_OBRA", "TRABAJADOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/inventory/insumos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/inventory/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/inventory/**").hasAnyRole("ADMIN", "SUPERVISOR_OBRA")
                // General role rules
                .requestMatchers("/api/v1/mobile/**").hasAnyRole("ADMIN", "SUPERVISOR_OBRA")
                .requestMatchers("/api/v1/projects/**").hasAnyRole("ADMIN", "SUPERVISOR_OBRA")
                .requestMatchers("/api/v1/evidence/**").hasAnyRole("ADMIN", "SUPERVISOR_OBRA")
                .requestMatchers("/api/v1/dashboard/**").hasAnyRole("ADMIN", "INVERSIONISTA")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}