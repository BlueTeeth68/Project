package com.mangareader.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticateFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //get JWT token String
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        //if the request does not have JWT, skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //if request have Jwt, get the jwt String
        jwt = authHeader.substring(7);
        if (SecurityContextHolder.getContext().getAuthentication() == null && jwtService.isAccessTokenValid(jwt)) {
            userName = jwtService.extractAccessUserName(jwt);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
