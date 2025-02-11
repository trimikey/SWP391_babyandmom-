package com.swp.BabyandMom.Filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.ExceptionHandler.ErrorResponseUtil;
import com.swp.BabyandMom.ExceptionHandler.InvalidToken;
import com.swp.BabyandMom.Service.JWTService;
import com.swp.BabyandMom.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    @Lazy
    private UserService accountService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token= null;
        String email = null;
        String uri = request.getRequestURI();
        if (uri.contains("/api/login") || uri.contains("/api/register")) {
            filterChain.doFilter(request,response);
            return;
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtService.extractEmail(token);
            } catch (TokenExpiredException e) {
                // Handle expired token
                ResponseEntity<String> responseEntity = ErrorResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Token has expired");
                response.setStatus(responseEntity.getStatusCodeValue());
                response.getWriter().write(responseEntity.getBody());
                return;
            } catch (InvalidToken e) {
                // Handle invalid token
                ResponseEntity<String> responseEntity = ErrorResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid token");
                response.setStatus(responseEntity.getStatusCodeValue());
                response.getWriter().write(responseEntity.getBody());
                return;
            }
        }

        if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            User account = accountService.getAccountByEmail(email);
            if(jwtService.validateToken(token, account)){
                UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(account,null, account.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
