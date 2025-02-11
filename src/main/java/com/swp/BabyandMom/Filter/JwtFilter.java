package com.swp.BabyandMom.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Service.JWTService;
import com.swp.BabyandMom.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.contains("/api/login") || uri.contains("/api/register")) {
            filterChain.doFilter(request,response);

            return;
        }
        System.out.println("Request URI: " + uri);



        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getAccountByEmail(email);
                if (user != null && jwtService.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid or expired token");
            error.put("message", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}