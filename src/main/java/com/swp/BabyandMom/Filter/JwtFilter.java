package com.swp.BabyandMom.Filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.InvalidToken;
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
import org.springframework.lang.NonNull;

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
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        
        // Cho phép các endpoint công khai
        if (uri.contains("/api/login") || 
            uri.contains("/api/register") || 
            uri.contains("/v3/api-docs") || 
            uri.contains("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token và endpoint yêu cầu xác thực
            if (uri.contains("/api/password")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized - Token required");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7); // Lấy token sau "Bearer "
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
        } catch (TokenExpiredException e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Expired token");
            error.put("message", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (InvalidToken e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid token");
            error.put("message", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized");
            error.put("message", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

}