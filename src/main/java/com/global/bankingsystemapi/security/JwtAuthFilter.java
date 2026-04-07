package com.global.bankingsystemapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        System.out.println("AUTH HEADER: " + authHeader);  // ← add this
        // 2. No header or wrong format → skip
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract token (remove"Bearer ")
        String token = authHeader.substring(7);

        // 4. Validate token
        if (jwtUtil.isTokenValid(token)) {

            // 5. Get username from token
            String username =
                    jwtUtil.extractUsername(token);

            // 6. Load user details from DB
            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            // 7. Build authentication object
            // authentication
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            // 8. Tell Spring this user is authenticated
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
        }

        // 9. Always continue chain
        filterChain.doFilter(request, response);
    }
}

