    package com.swp.BabyandMom.Filter;

    import com.auth0.jwt.exceptions.TokenExpiredException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.swp.BabyandMom.Entity.User;
    import com.swp.BabyandMom.ExceptionHandler.AuthException;
    import com.swp.BabyandMom.ExceptionHandler.InvalidToken;
    import com.swp.BabyandMom.Service.JWTService;
    import com.swp.BabyandMom.Service.UserService;
    import io.jsonwebtoken.ExpiredJwtException;
    import io.jsonwebtoken.MalformedJwtException;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.util.AntPathMatcher;
    import org.springframework.web.filter.OncePerRequestFilter;
    import org.springframework.lang.NonNull;
    import org.springframework.web.servlet.HandlerExceptionResolver;

    import java.io.IOException;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    @Component
    public class JwtFilter extends OncePerRequestFilter {

        @Autowired
        @Qualifier("handlerExceptionResolver")
        HandlerExceptionResolver resolver;

        @Autowired
        JWTService jwtService;

        List<String> PUBLIC_API = List.of(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/api/login",
                "/api/register",
                "/api/user/forgot",
                "/api/user/reset",
                "/api/order/cancel/**",
                "/api/order/payment-success/**"
        );

        //nhận bt api nào là api cần phân quyền
        boolean isPermitted(HttpServletRequest request) {
            AntPathMatcher patchMatch = new AntPathMatcher();
            String uri = request.getRequestURI();
            String method = request.getMethod(); //post, put, delete

            if(method.equals("GET") && patchMatch.match("/api/product/**", uri)) {
                return true; //public api (còn api khác thì check như cũ)
            }

            return PUBLIC_API.stream().anyMatch(item -> patchMatch.match(item, uri));
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            // check trước khi cho truy cập

            String uri = request.getRequestURI();
            if (isPermitted(request)) {
                // public API
                filterChain.doFilter(request, response);
            } else {
                // không phải là public API => check role
                String token = getToken(request);

                if (token == null) {
                    // chưa đăng nhập => quăng lỗi
                    resolver.resolveException(request, response, null, new AuthException("Authentication token is missing!"));
                }

                User account = null;
                try {
                    account = jwtService.getAccountByToken(token);
                } catch (MalformedJwtException malformedJwtException) {
                    resolver.resolveException(request, response, null, new AuthException("Authentication token is invalid!"));
                } catch (ExpiredJwtException expiredJwtException) {
                    resolver.resolveException(request, response, null, new AuthException("Authentication token is expired!"));
                } catch (Exception exception) {
                    resolver.resolveException(request, response, null, new AuthException("Authentication token is invalid!"));
                }

                // => token chuẩn
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account, token, account.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }
        }

        String getToken(HttpServletRequest request) {
 
            String token = request.getHeader("Authorization");
            if(token == null) return null;
            return token.substring(7);
        }

    }