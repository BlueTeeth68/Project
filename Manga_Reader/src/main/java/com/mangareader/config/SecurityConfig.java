package com.mangareader.config;

import com.mangareader.security.jwt.JWTAuthenticateFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticateFilter jwtAuthenticateFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //disable csrf
                .csrf().disable()
                //add our custom filter before UsernamePasswordAuthenticationFilter
//                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
                //set session to stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                //permit all request from /api/auth/
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/manga/**").permitAll()
                .requestMatchers("/genre/**").permitAll()
                .requestMatchers("/keyword/**").permitAll()
                .requestMatchers("/author/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                // any request must be authenticated
                .anyRequest()
                .authenticated()
        ;

        return http.build();
    }

}
