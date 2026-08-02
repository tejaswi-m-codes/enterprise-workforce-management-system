package com.tejaswi.enterpriseworkforcemanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final ForcePasswordChangeFilter forcePasswordChangeFilter;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter,
                                                JwtAuthenticationEntryPoint authenticationEntryPoint,
                      CustomAccessDeniedHandler accessDeniedHandler,
                    ForcePasswordChangeFilter forcePasswordChangeFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
        this.authenticationEntryPoint=authenticationEntryPoint;
        this.accessDeniedHandler=accessDeniedHandler;
        this.forcePasswordChangeFilter=forcePasswordChangeFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();

    }

    /*

ADMIN
System-level authority.

Responsibilities:

Create/update/delete departments
Create managers
Create employees
Create projects
Assign roles
Delete critical data

MANAGER
Business/team authority.

Responsibilities:

Create employees under their department
Update employee details
Assign projects to employees
View employees
View projects


EMPLOYEE
Normal user.

Responsibilities:

View own profile
View assigned projects
Update own details
Change password
    
    
    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .csrf(csrf->csrf.disable())
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth-> auth
            .requestMatchers("/auth/signin",
        "/auth/forgot-password",
        "/auth/reset-password",
        "/auth/refresh-token",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex->ex
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        )
       .authenticationProvider(authenticationProvider()) //its like whenever authentication required, use this provider
       .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
       .addFilterAfter(forcePasswordChangeFilter,JwtAuthenticationFilter.class);
        
        return http.build();
    }



}
