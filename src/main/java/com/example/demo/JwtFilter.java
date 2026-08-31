package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String sessionId = request.getHeader("X-Session-Id");
        String securityCode = request.getHeader("X-Security-Code");

        System.out.println("=== JwtFilter running ===");
        System.out.println("Request URL: " + request.getRequestURI());
        System.out.println("Authorization header: " + authHeader);
        System.out.println("X-Session-Id: " + sessionId);
        System.out.println("X-Security-Code: " + securityCode);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                boolean sessionOk = sessionId != null && authService.isSessionValid(username, sessionId);
                boolean securityOk = securityCode != null && authService.isSecurityCodeValid(username, securityCode);

                System.out.println("Token valid, session valid? " + sessionOk + ", security code valid? " + securityOk);

                if (sessionOk && securityOk) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set in SecurityContext!");
                } else {
                    System.out.println("Session or Security Code invalid!");
                }
            } else {
                System.out.println("Token validation FAILED!");
            }
        } else {
            System.out.println("No Bearer token found in header!");
        }

        filterChain.doFilter(request, response);
    }
}