package com.mangareader.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangareader.exception.ErrorDetails;
import com.mangareader.security.jwt.JWTAuthenticateFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SecurityConfig {

    private final JWTAuthenticateFilter jwtAuthenticateFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //disable csrf
                .csrf().disable()
                //add our custom filter before UsernamePasswordAuthenticationFilter
                //.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class)
                //set session to stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/manga/**").permitAll()
                .requestMatchers("/genre/**").permitAll()
                .requestMatchers("/keyword/**").permitAll()
                .requestMatchers("/author/**").hasAnyAuthority("ADMIN", "TRANSLATOR")
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest()
//                .authenticated()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
                    if (ex instanceof BadCredentialsException) {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        response.getWriter().write(new ObjectMapper()
                                .writeValueAsString(new ErrorDetails(HttpStatus.BAD_REQUEST, "Bad credential for Bearer token", ex.getMessage())));
                    } else {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write(new ObjectMapper()
                                .writeValueAsString(new ErrorDetails(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage())));
                    }
                })
                .and()
                .exceptionHandling()
                .accessDeniedHandler(
                        (request, response, ex) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write(new ObjectMapper()
                                    .writeValueAsString(new ErrorDetails(HttpStatus.FORBIDDEN, "You do not have authority to access this resource", ex.getMessage())));
                        }
                )
        ;

        return http.build();
    }

}
