package com.rafael.nailspro.webapp.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityFilter filter;
    private final TenantIdFilter tenantIdFilter;
    private final TenantStatusFilter tenantStatusFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        int cost = 12;
        return new BCryptPasswordEncoder(cost);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // Admin Pages
                        .requestMatchers(HttpMethod.GET,
                                "/admin/servicos",
                                "/admin/configuracoes").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/admin/agenda-profissional").hasAnyRole("ADMIN", "PROFESSIONAL")

                        // APIs
                        .requestMatchers("/api/internal/**",
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/api/v1/webhook/**",
                        "/evolution-test").permitAll() //todo: remember to remove the test page

                        // User/Anonymous Pages
                        .requestMatchers(HttpMethod.GET,
                                "/entrar",
                                "/cadastro",
                                "/agendar",
                                "/actuator/health")
                        .permitAll()

                        .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                // .addFilterAfter(tenantIdFilter, filter.getClass())
                // .addFilterAfter(tenantStatusFilter, tenantIdFilter.getClass())
                .build();
    }
}